package com.despance.salesapp.data;

import com.despance.salesapp.modal.CartItem.CartItem;
import com.despance.salesapp.modal.Product.Product;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class TLVObject {


    public enum TLVTag {
        ID, NAME, PRICE, VATRATE, BARCODE, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, USER, PRODUCT, ARRAY, PAYMENT,
        PAYMENT_TYPE, PAYMENT_AMOUNT, PRODUCT_AMOUNT, TIMESTAMP, DESCRIPTION, PRODUCT_ID, CREDIT_TOTAL, CASH_TOTAL,
        QR_TOTAL, CART_ITEM, RECEIPT, DATE
    }

    private TLVTag tag;
    private short length;
    private byte[] value;
    private TLVObject[] childs;

    public TLVObject() {
    }

    public TLVObject(Payment payment) {
        this.tag = TLVTag.PAYMENT;
        TLVObject type = new TLVObject(TLVTag.PAYMENT_TYPE, payment.getType());
        TLVObject description = new TLVObject(TLVTag.DESCRIPTION, payment.getDescription());
        TLVObject amount = new TLVObject(TLVTag.PAYMENT_AMOUNT, payment.getAmount());
        TLVObject timestamp = new TLVObject(TLVTag.DATE, payment.getTimestamp());
        this.value = concatTLVObjects(type, description, amount, timestamp);
        this.childs = new TLVObject[] { type, description, amount, timestamp };
        this.length = (short) value.length;

    }

    public TLVObject(User user) {
        this.tag = TLVTag.USER;
        TLVObject id = new TLVObject(TLVTag.ID, user.getId());
        TLVObject firstName = new TLVObject(TLVTag.FIRST_NAME, user.getFirstName());
        TLVObject lastName = new TLVObject(TLVTag.LAST_NAME, user.getLastName());
        TLVObject email = new TLVObject(TLVTag.EMAIL, user.getEmail());
        TLVObject password = new TLVObject(TLVTag.PASSWORD, user.getPassword());
        this.value = concatTLVObjects(id, firstName, lastName, email, password);
        this.childs = new TLVObject[] { id, firstName, lastName, email, password };
        this.length = (short) value.length;
    }

    public TLVObject(Product product) {
        this.tag = TLVTag.PRODUCT;
        TLVObject id = new TLVObject(TLVTag.ID, product.getId());
        TLVObject name = new TLVObject(TLVTag.NAME, product.getProductName());
        TLVObject price = new TLVObject(TLVTag.PRICE, product.getPrice());
        TLVObject vatRate = new TLVObject(TLVTag.VATRATE, product.getVatRate());
        TLVObject barcode = new TLVObject(TLVTag.BARCODE, product.getBarcode());
        this.value = concatTLVObjects(id, name, price, vatRate, barcode);
        this.childs = new TLVObject[] { id, name, price, vatRate, barcode };
        this.length = (byte) value.length;
    }

    public TLVObject (CartItem[] cartItems){
        TLVObject[] childs = new TLVObject[cartItems.length];
        for (int i = 0; i < childs.length; i++) {
            childs[i] = new TLVObject(cartItems[i]);
        }
        this.tag = TLVTag.ARRAY;
        this.childs = childs;
        this.value = concatTLVObjects(childs);
        this.length = (short) value.length;
    }
    public TLVObject(Product[] products) {
        TLVObject[] childs = new TLVObject[products.length];
        for (int i = 0; i < childs.length; i++) {
            childs[i] = new TLVObject(products[i]);
        }
        this.tag = TLVTag.ARRAY;
        this.childs = childs;
        this.value = concatTLVObjects(childs);
        this.length = (short) value.length;
    }

    public TLVObject(User[] users) {
        TLVObject[] childs = new TLVObject[users.length];
        for (int i = 0; i < childs.length; i++) {
            childs[i] = new TLVObject(users[i]);
        }
        this.tag = TLVTag.ARRAY;
        this.childs = childs;
        this.value = concatTLVObjects(childs);
        this.length = (short) value.length;
    }

    public TLVObject(TLVTag tag, short length, byte[] value) {
        this.tag = tag;
        this.length = length;
        this.value = value;
    }

    TLVObject(TLVTag tag, String value) {
        this.tag = tag;
        setValue(value);
        this.length = (short) value.length();
    }

    TLVObject(TLVTag tag, int value) {
        this.tag = tag;
        setValue(value);
        this.length = (short) String.valueOf(value).length();
    }

    TLVObject(TLVTag tag, float value) {
        this.tag = tag;
        setValue(value);
        this.length = (short) String.valueOf(value).length();
    }


    TLVObject(CartItem cartItem){
        this.tag = TLVTag.CART_ITEM;
        TLVObject productId = new TLVObject(TLVTag.PRODUCT_ID, cartItem.getProduct().getId());
        TLVObject productAmount = new TLVObject(TLVTag.PRODUCT_AMOUNT, cartItem.getQuantity());
        this.value = concatTLVObjects(productId, productAmount);
        this.childs = new TLVObject[] { productId, productAmount };
        this.length = (short) value.length;
    }
    public TLVObject(Receipt receipt){
        this.tag = TLVTag.RECEIPT;


        TLVObject id = new TLVObject(TLVTag.ID, receipt.getUserId());
        TLVObject timestamp = new TLVObject(TLVTag.TIMESTAMP, receipt.getTimestamp());

        TLVObject childs = new TLVObject(receipt.getCartItems());

        TLVObject creditTotal = new TLVObject(TLVTag.CREDIT_TOTAL, receipt.getCreditTotal());
        TLVObject cashTotal = new TLVObject(TLVTag.CASH_TOTAL, receipt.getCashTotal());
        TLVObject qrTotal = new TLVObject(TLVTag.QR_TOTAL, receipt.getQrTotal());

        this.value = concatTLVObjects(id, timestamp, childs, creditTotal, cashTotal, qrTotal);
        this.childs = new TLVObject[] { id, timestamp, childs, creditTotal, cashTotal, qrTotal };
        this.length = (short) value.length;
    }
    public TLVTag getTag() {
        return tag;
    }

    public void setTag(TLVTag tag) {
        this.tag = tag;
    }

    public int getLength() {
        return length;
    }

    public void setLength(byte length) {
        this.length = length;
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }

    public void setValue(String value) {
        this.value = value.getBytes();
    }

    public void setValue(int value) {
        this.value = String.valueOf(value).getBytes();
    }

    public void setValue(float value) {
        this.value = String.valueOf(value).getBytes();
    }

    public byte[] encode() {
        byte[] encoded = new byte[4 + length];
        encoded[0] = (byte) (tag.ordinal() >> 8);
        encoded[1] = (byte) (tag.ordinal());
        encoded[2] = (byte) (length >> 8);
        encoded[3] = (byte) (length);
        System.arraycopy(value, 0, encoded, 4, length);
        return encoded;
    }

    private byte[] concatTLVObjects(TLVObject... tlvObjects) {
        int totalLength = 0;
        for (TLVObject tlvObject : tlvObjects) {
            totalLength += tlvObject.encode().length;
        }
        byte[] concatenated = new byte[totalLength];
        int index = 0;
        for (TLVObject tlvObject : tlvObjects) {
            System.arraycopy(tlvObject.encode(), 0, concatenated, index, tlvObject.encode().length);
            index += tlvObject.encode().length;
        }
        return concatenated;
    }

    public static String byteToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString();
    }



    public String byteToHexString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%04X ", tag.ordinal()));
        sb.append(String.format("%04X ", length));

        for (byte b : value) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString();
    }

    public String byteToHexFormattedString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%04X ", tag.ordinal()));
        sb.append(String.format("%04X\n", length));
        if (childs == null) {
            for (Byte b : value) {
                sb.append(String.format("%02X ", b));
            }
        } else
            for (TLVObject tlvObject : childs) {
                sb.append(tlvObject.byteToHexFormattedString(1));
            }
        return sb.toString();
    }

    private String byteToHexFormattedString(int depth) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            sb.append("\t");
        }
        sb.append(String.format("%04X ", tag.ordinal()));
        sb.append(String.format("%04X\n", length));

        if (childs == null) {
            for (int i = 0; i < depth + 1; i++) {
                sb.append("\t");
            }
            for (Byte b : value) {
                sb.append(String.format("%02X ", b));
            }
            sb.append("\n");
        } else
            for (TLVObject tlvObject : childs) {
                sb.append(tlvObject.byteToHexFormattedString(depth + 1));
            }

        return sb.toString();
    }

    public static Object decode(byte[] bytes) {
        int byte1 = bytes[0];
        int byte2 = bytes[1];
        int tagVal = (byte1 << 8) + byte2;
        TLVTag tag = TLVTag.values()[tagVal];
        short length = (short) ((bytes[2] << 8) + bytes[3]);
        AtomicInteger currentIndex = new AtomicInteger(4);

        switch (tag) {
            case ID:
                return Integer.parseInt(new String(bytes, 4, length));
            case PRICE:
            case VATRATE:
            case PAYMENT_AMOUNT:
                return Float.parseFloat(new String(bytes, 4, length));
            case BARCODE:
            case NAME:
            case FIRST_NAME:
            case LAST_NAME:
            case EMAIL:
            case PASSWORD:
                return new String(bytes, 4, length);
            case USER:
                int userId = (int) decode(bytes, currentIndex);
                String userFirstName = (String) decode(bytes, currentIndex);
                String userLastName = (String) decode(bytes, currentIndex);
                String userEmail = (String) decode(bytes, currentIndex);
                String userPassword = (String) decode(bytes, currentIndex);
                return new User(userId, userFirstName, userLastName, userEmail, userPassword);
            case PRODUCT:
                int productId = (int) decode(bytes, currentIndex);
                String productName = (String) decode(bytes, currentIndex);
                float productPrice = (float) decode(bytes, currentIndex);
                float productVatRate = (float) decode(bytes, currentIndex);
                String productBarcode = (String) decode(bytes, currentIndex);
                return new Product(productId, productName, productPrice, productVatRate, productBarcode);
            case ARRAY:
                ArrayList<Object> childs = new ArrayList<>();
                while (currentIndex.get() < bytes.length)
                    childs.add(decode(bytes, currentIndex));
                return childs;
            case PAYMENT:
                String paymentType = (String) decode(bytes, currentIndex);
                float paymentAmount = (float) decode(bytes, currentIndex);
                String paymentTimestamp = (String) decode(bytes, currentIndex);
                String paymentDescription = (String) decode(bytes, currentIndex);
                return new Payment(paymentType, paymentDescription, paymentAmount, paymentTimestamp);
            default:
                return null;

        }

    }

    private static Object decode(byte[] bytes, AtomicInteger indexObj) {
        int index = indexObj.get();
        TLVTag tag = TLVTag.values()[(bytes[index] << 8) + bytes[index + 1]];
        short length = (short) ((bytes[index + 2] << 8) + bytes[index + 3]);
        index += 4;
        indexObj.set(index + length);
        switch (tag) {
            case ID:
                return Integer.parseInt(new String(bytes, index, length));
            case PRICE:
            case VATRATE:
            case CREDIT_TOTAL:
            case CASH_TOTAL:
            case QR_TOTAL:
            case PAYMENT_AMOUNT:
                return Float.parseFloat(new String(bytes, index, length));
            case BARCODE:
            case NAME:
            case FIRST_NAME:
            case LAST_NAME:
            case EMAIL:
            case PASSWORD:
            case PAYMENT_TYPE:
            case DESCRIPTION:
            case TIMESTAMP:
                return new String(bytes, index, length);
            case USER:
                indexObj.set(index);
                int userId = (int) decode(bytes, indexObj);
                String userFirstName = (String) decode(bytes, indexObj);
                String userLastName = (String) decode(bytes, indexObj);
                String userEmail = (String) decode(bytes, indexObj);
                String userPassword = (String) decode(bytes, indexObj);
                return new User(userId, userFirstName, userLastName, userEmail, userPassword);
            case PRODUCT:
                indexObj.set(index);
                int productId = (int) decode(bytes, indexObj);
                String productName = (String) decode(bytes, indexObj);
                float productPrice = (float) decode(bytes, indexObj);
                float productVatRate = (float) decode(bytes, indexObj);
                String productBarcode = (String) decode(bytes, indexObj);
                return new Product(productId, productName, productPrice, productVatRate, productBarcode);
            default:
                return null;

        }

    }

}

package com.despance.salesapp.utils;

import com.despance.salesapp.data.*;
import com.despance.salesapp.modal.Product.Product;
import com.despance.salesapp.modal.CartItem.CartItem;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;



public final class TLVUtils {

    public enum TLVTag {
        ID, NAME, PRICE, VATRATE, BARCODE, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, USER, PRODUCT, ARRAY, PAYMENT,
        PAYMENT_TYPE, PAYMENT_AMOUNT, PRODUCT_QUANTITY, TIMESTAMP, DESCRIPTION, USER_ID, PRODUCT_ID, CREDIT_TOTAL,
        CASH_TOTAL, QR_TOTAL, CART_ITEM, RECEIPT, DATE
    }

    private TLVUtils() {
    }

    public static byte[] encode(User user) {
        byte[] tagBytes = setTag(TLVTag.USER);
        byte[] idBytes = encode(TLVTag.ID, user.getId());
        byte[] firstNameBytes = encode(TLVTag.FIRST_NAME, user.getFirstName());
        byte[] lastNameBytes = encode(TLVTag.LAST_NAME, user.getLastName());
        byte[] emailBytes = encode(TLVTag.EMAIL, user.getEmail());
        byte[] passwordBytes = encode(TLVTag.PASSWORD, user.getPassword());
        byte[] lengthBytes = setLength(
                idBytes.length + firstNameBytes.length + lastNameBytes.length + emailBytes.length
                        + passwordBytes.length);
        return concatBytes(tagBytes, lengthBytes, idBytes, firstNameBytes, lastNameBytes, emailBytes, passwordBytes);
    }

    public static byte[] encode(User[] users) {
        byte[] tagBytes = setTag(TLVTag.ARRAY);
        byte[] userBytes = new byte[0];
        for (User user : users) {
            userBytes = concatBytes(userBytes, encode(user));
        }
        byte[] lengthBytes = setLength(userBytes.length);
        return concatBytes(tagBytes, lengthBytes, userBytes);
    }

    public static byte[] encode(Product product) {
        byte[] tagBytes = setTag(TLVTag.PRODUCT);
        byte[] idBytes = encode(TLVTag.ID, product.getId());
        byte[] nameBytes = encode(TLVTag.NAME, product.getProductName());
        byte[] priceBytes = encode(TLVTag.PRICE, product.getPrice());
        byte[] vatRateBytes = encode(TLVTag.VATRATE, product.getVatRate());
        byte[] barcodeBytes = encode(TLVTag.BARCODE, product.getBarcode());
        byte[] lengthBytes = setLength(
                idBytes.length + nameBytes.length + priceBytes.length + vatRateBytes.length + barcodeBytes.length);
        return concatBytes(tagBytes, lengthBytes, idBytes, nameBytes, priceBytes, vatRateBytes, barcodeBytes);
    }

    public static byte[] encode(Product[] products) {
        byte[] tagBytes = setTag(TLVTag.ARRAY);
        byte[] productBytes = new byte[0];
        for (Product product : products) {
            productBytes = concatBytes(productBytes, encode(product));
        }
        byte[] lengthBytes = setLength(productBytes.length);
        return concatBytes(tagBytes, lengthBytes, productBytes);
    }

    public static byte[] encode(Payment payment) {
        byte[] tagBytes = setTag(TLVTag.PAYMENT);
        byte[] typeBytes = encode(TLVTag.PAYMENT_TYPE, payment.getType());
        byte[] descriptionBytes = encode(TLVTag.DESCRIPTION, payment.getDescription());
        byte[] amountBytes = encode(TLVTag.PAYMENT_AMOUNT, payment.getAmount());
        byte[] lengthBytes = setLength(typeBytes.length + descriptionBytes.length + amountBytes.length);
        return concatBytes(tagBytes, lengthBytes, typeBytes, descriptionBytes, amountBytes);
    }

    public static byte[] encode(Payment[] payments) {
        byte[] tagBytes = setTag(TLVTag.ARRAY);
        byte[] paymentBytes = new byte[0];
        for (Payment payment : payments) {
            paymentBytes = concatBytes(paymentBytes, encode(payment));
        }
        byte[] lengthBytes = setLength(paymentBytes.length);
        return concatBytes(tagBytes, lengthBytes, paymentBytes);
    }

    public static byte[] encode(CartItem cartItem) {
        byte[] tagBytes = setTag(TLVTag.CART_ITEM);
        byte[] productBytes = encode(cartItem.getProduct());
        byte[] amountBytes = encode(TLVTag.PRODUCT_QUANTITY, cartItem.getQuantity());
        byte[] lengthBytes = setLength(productBytes.length + amountBytes.length);
        return concatBytes(tagBytes, lengthBytes, productBytes, amountBytes);

    }

    public static byte[] encode(CartItem[] cartItems) {
        byte[] tagBytes = setTag(TLVTag.ARRAY);
        byte[] cartItemBytes = new byte[0];
        for (CartItem cartItem : cartItems) {
            cartItemBytes = concatBytes(cartItemBytes, encode(cartItem));
        }
        byte[] lengthBytes = setLength(cartItemBytes.length);
        return concatBytes(tagBytes, lengthBytes, cartItemBytes);
    }

    public static byte[] encode(Receipt receipt) {
        byte[] tagBytes = setTag(TLVTag.RECEIPT);
        byte[] idBytes = encode(TLVTag.ID, receipt.getId());
        byte[] userIdBytes = encode(TLVTag.USER_ID, receipt.getUserId());
        byte[] dateBytes = encode(TLVTag.DATE, receipt.getTimestamp());
        byte[] creditTotalBytes = encode(TLVTag.CREDIT_TOTAL, receipt.getCreditTotal());
        byte[] cashTotalBytes = encode(TLVTag.CASH_TOTAL, receipt.getCashTotal());
        byte[] qrTotalBytes = encode(TLVTag.QR_TOTAL, receipt.getQrTotal());
        byte[] cartItemsBytes = encode(receipt.getCartItems());
        byte[] lengthBytes = setLength(idBytes.length + userIdBytes.length + dateBytes.length + creditTotalBytes.length
                + cashTotalBytes.length + qrTotalBytes.length + cartItemsBytes.length);
        return concatBytes(tagBytes, lengthBytes, idBytes, userIdBytes, dateBytes, creditTotalBytes, cashTotalBytes,
                qrTotalBytes, cartItemsBytes);
    }

    public static byte[] encode(Receipt[] receipts) {
        byte[] tagBytes = setTag(TLVTag.ARRAY);
        byte[] receiptBytes = new byte[0];
        for (Receipt receipt : receipts) {
            receiptBytes = concatBytes(receiptBytes, encode(receipt));
        }
        byte[] lengthBytes = setLength(receiptBytes.length);
        return concatBytes(tagBytes, lengthBytes, receiptBytes);
    }

    private static byte[] encode(TLVTag tag, int value) {
        byte[] tagBytes = setTag(tag);
        byte[] valueBytes = String.valueOf(value).getBytes();
        byte[] lengthBytes = setLength(valueBytes.length);
        return concatBytes(tagBytes, lengthBytes, valueBytes);
    }

    private static byte[] encode(TLVTag tag, float value) {
        byte[] tagBytes = setTag(tag);
        byte[] valueBytes = String.valueOf(value).getBytes();
        byte[] lengthBytes = setLength(valueBytes.length);
        return concatBytes(tagBytes, lengthBytes, valueBytes);
    }

    private static byte[] encode(TLVTag tag, String value) {
        byte[] tagBytes = setTag(tag);
        byte[] valueBytes = value.getBytes();
        byte[] lengthBytes = setLength(valueBytes.length);
        return concatBytes(tagBytes, lengthBytes, valueBytes);
    }

    private static byte[] setLength(int length) {
        byte[] lengthBytes = new byte[2];
        lengthBytes[0] = (byte) (length >> 8);
        lengthBytes[1] = (byte) length;

        return lengthBytes;
    }

    private static byte[] concatBytes(byte[]... bytes) {
        int length = 0;
        for (byte[] b : bytes)
            length += b.length;

        byte[] result = new byte[length];
        int pos = 0;
        for (byte[] b : bytes) {
            System.arraycopy(b, 0, result, pos, b.length);
            pos += b.length;
        }
        return result;
    }

    private static byte[] setTag(TLVTag tag) {
        byte[] tagBytes = new byte[2];
        tagBytes[0] = (byte) (tag.ordinal() >> 8);
        tagBytes[1] = (byte) tag.ordinal();

        return tagBytes;
    }

    public static Object decode(byte[] bytes) {
        int tag = (bytes[0] << 8) + bytes[1];
        AtomicInteger offset = new AtomicInteger(4);
        switch (TLVTag.values()[tag]) {
            case USER:
                return decodeUser(bytes, offset);
            case ARRAY:
                return decodeArray(bytes, offset);
            case PRODUCT:
                return decodeProduct(bytes, offset);
            case PAYMENT:
                return decodePayment(bytes, offset);
            case CART_ITEM:
                return decodeCartItem(bytes, offset);
            case RECEIPT:
                return decodeReceipt(bytes, offset);
            default:
                throw new IllegalArgumentException("Unexpected value: " + TLVTag.values()[tag].toString()
                        + " while decoding the object. Tag number: " + tag);
        }
    }

    private static User decodeUser(byte[] bytes, AtomicInteger offset) {
        int index = offset.get() - 2;
        int length = (bytes[index] << 8) + (bytes[index + 1] & 0xff);
        index += 2;
        offset.set(index);
        User user = new User();
        while (offset.get() < index + length) {
            Pair decoded = decodePrimitive(bytes, offset);
            switch (TLVTag.values()[decoded.getTag()]) {
                case ID:
                    user.setId((int) decoded.getValue());
                    break;
                case FIRST_NAME:
                    user.setFirstName((String) decoded.getValue());
                    break;
                case LAST_NAME:
                    user.setLastName((String) decoded.getValue());
                    break;
                case EMAIL:
                    user.setEmail((String) decoded.getValue());
                    break;
                case PASSWORD:
                    user.setPassword((String) decoded.getValue());
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected value: " + TLVTag.values()[decoded.getTag()]
                            + " while decoding user. Tag number: " + decoded.getTag());
            }
        }
        offset.set(index + length);
        return user;
    }

    private static Product decodeProduct(byte[] bytes, AtomicInteger offset) {
        int index = offset.get() - 2;
        int length = (bytes[index] << 8) + (bytes[index + 1] & 0xff);
        index += 2;
        offset.set(index);
        Product product = new Product();
        while (offset.get() < index + length) {
            Pair decoded = decodePrimitive(bytes, offset);

            switch (TLVTag.values()[decoded.getTag()]) {
                case ID:
                    product.setId((int) decoded.getValue());
                    break;
                case NAME:
                    product.setProductName((String) decoded.getValue());
                    break;
                case PRICE:
                    product.setPrice((float) decoded.getValue());
                    break;
                case VATRATE:
                    product.setVatRate((float) decoded.getValue());
                    break;
                case BARCODE:
                    product.setBarcode((String) decoded.getValue());
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected value: " + TLVTag.values()[decoded.getTag()]
                            + " while decoding product. Tag number: " + decoded.getTag());
            }
        }
        offset.set(index + length);
        return product;

    }

    private static Payment decodePayment(byte[] bytes, AtomicInteger offset) {
        int index = offset.get() - 2;
        int length = (bytes[index] << 8) + (bytes[index + 1] & 0xff);
        index += 2;
        offset.set(index);
        Payment payment = new Payment();
        while (offset.get() < index + length) {
            Pair decoded = decodePrimitive(bytes, offset);
            switch (TLVTag.values()[decoded.getTag()]) {
                case PAYMENT_TYPE:
                    payment.setType((String) decoded.getValue());
                    break;
                case DESCRIPTION:
                    payment.setDescription((String) decoded.getValue());
                    break;
                case PAYMENT_AMOUNT:
                    payment.setAmount((float) decoded.getValue());
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected value: " + TLVTag.values()[decoded.getTag()]
                            + " while decoding payment. Tag number: " + decoded.getTag());
            }
        }
        return payment;

    }

    private static CartItem decodeCartItem(byte[] bytes, AtomicInteger offset) {
        int index = offset.get() - 2;
        int length = (bytes[index] << 8) + (bytes[index + 1] & 0xff);
        index += 2;
        offset.set(index);
        CartItem cartItem = new CartItem();
        while (offset.get() < index + length) {
            Pair decoded = decodePrimitive(bytes, offset);
            switch (TLVTag.values()[decoded.getTag()]) {
                case PRODUCT_ID:
                    throw new UnsupportedOperationException("Product id is not implemented in android version.");
                case PRODUCT:
                    cartItem.setProduct((Product) decoded.getValue());
                    break;
                case PRODUCT_QUANTITY:
                    cartItem.setQuantity((int) decoded.getValue());
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected value: " + TLVTag.values()[decoded.getTag()]
                            + " while decoding cart item. Tag number: " + decoded.getTag());
            }
        }
        offset.set(index + length);
        return cartItem;
    }

    private static Receipt decodeReceipt(byte[] bytes, AtomicInteger offset) {
        int index = offset.get() - 2;
        int length = (bytes[index] << 8) + (bytes[index + 1] & 0xff);
        index += 2;
        offset.set(index);
        Receipt receipt = new Receipt();
        while (offset.get() < index + length) {
            Pair decoded = decodePrimitive(bytes, offset);
            switch (TLVTag.values()[decoded.getTag()]) {
                case ID:
                    receipt.setId((int) decoded.getValue());
                    break;
                case USER_ID:
                    throw new UnsupportedOperationException("User id is not implemented in android version.");
                case DATE:
                    receipt.setTimestamp((String) decoded.getValue());
                    break;
                case CREDIT_TOTAL:
                    receipt.setCreditTotal((float) decoded.getValue());
                    break;
                case CASH_TOTAL:
                    receipt.setCashTotal((float) decoded.getValue());
                    break;
                case QR_TOTAL:
                    receipt.setQrTotal((float) decoded.getValue());
                    break;
                case ARRAY:
                    ArrayList<CartItem> cartItems = new ArrayList<>();
                    for (Object o : (ArrayList<?>) decoded.getValue()) {
                        if (o instanceof CartItem)
                            cartItems.add((CartItem) o);
                    }
                    receipt.setCartItems(cartItems.toArray(new CartItem[cartItems.size()]));
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected value: " + TLVTag.values()[decoded.getTag()]
                            + " while decoding receipt. Tag number: " + decoded.getTag());
            }
        }
        offset.set(index + length);
        return receipt;
    }

    private static ArrayList<?> decodeArray(byte[] bytes, AtomicInteger offset) {
        int index = offset.get() - 2;
        int length = (bytes[index] << 8) + (bytes[index + 1] & 0xff);
        index += 2;
        offset.set(index);
        ArrayList<Object> list = new ArrayList<>();
        while (offset.get() < index + length) {
            Pair decoded = decodePrimitive(bytes, offset);
            switch (TLVTag.values()[decoded.getTag()]) {
                case USER:
                case PRODUCT:
                case PAYMENT:
                case CART_ITEM:
                case RECEIPT:
                    list.add(decoded.getValue());
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected value: " + TLVTag.values()[decoded.getTag()]
                            + " while decoding array. Tag number: " + decoded.getTag());
            }
        }
        offset.set(index + length);
        return list;
    }

    public static String byteToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString();
    }

    private static Pair decodePrimitive(byte[] bytes, AtomicInteger offset) {
        int index = offset.get();
        int tag = (bytes[index] << 8) + (bytes[index + 1] & 0xff);
        index += 2;
        int length = (bytes[index] << 8) + (bytes[index + 1] & 0xff);
        String value;
        index += 2;
        switch (TLVTag.values()[tag]) {
            case ID:
            case PRODUCT_ID:
            case PRODUCT_QUANTITY:
            case USER_ID:
                try {
                    value = new String(bytes, index, length,"windows-1252");
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
                offset.set(index + length);
                return new Pair(tag, Integer.parseInt(value));
            case FIRST_NAME:
            case LAST_NAME:
            case EMAIL:
            case PASSWORD:
            case NAME:
            case DESCRIPTION:
            case PAYMENT_TYPE:
            case BARCODE:
            case TIMESTAMP:
            case DATE:
                try {
                    value = new String(bytes, index, length,"windows-1252");
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
                offset.set(index + length);
                return new Pair(tag, value);
            case PRICE:
            case VATRATE:
            case PAYMENT_AMOUNT:
            case CREDIT_TOTAL:
            case CASH_TOTAL:
            case QR_TOTAL:
                try {
                    value = new String(bytes, index, length,"windows-1252");
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
                offset.set(index + length);
                return new Pair(tag, Float.parseFloat(value));
            case USER:
                offset.set(index);
                return new Pair(tag, decodeUser(bytes, offset));
            case PRODUCT:
                offset.set(index);
                return new Pair(tag, decodeProduct(bytes, offset));
            case PAYMENT:
                offset.set(index);
                return new Pair(tag, decodePayment(bytes, offset));
            case CART_ITEM:
                offset.set(index);
                return new Pair(tag, decodeCartItem(bytes, offset));
            case RECEIPT:
                offset.set(index);
                return new Pair(tag, decodeReceipt(bytes, offset));
            case ARRAY:
                offset.set(index);
                return new Pair(tag, decodeArray(bytes, offset));
            default:
                throw new IllegalArgumentException("Unexpected value: " + TLVTag.values()[tag].toString()
                        + " while decoding primitive data type. Tag number: " + tag);

        }

    }

}

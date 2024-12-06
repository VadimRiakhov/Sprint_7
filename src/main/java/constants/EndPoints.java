package constants;

public class EndPoints {
    public static final String COURIER_CREATE = "/api/v1/courier";
    public static final String COURIER_LOGIN = "/api/v1/courier/login";
    public static final String COURIER_DELETE = "/api/v1/courier/{id}";
    public static final String ORDER_CREATE = "/api/v1/orders";
    public static final String ORDER_GET_BY_TRACK = "/api/v1/orders/track?t={orderTrack}";
    public static final String ORDERS_GET = "/api/v1/orders";
    public static final String ORDER_ACCEPT = "/api/v1/orders/accept/{orderId}?courierId={courierId}";
    public static final String ORDER_FINISH = "/api/v1/orders/finish/{orderId}";
    public static final String ORDER_CANCEL = "/api/v1/orders/cancel?track={track}";

}

package com.iseva.app.source.travel;

/**
 * Created by xb_sushil on 1/20/2017.
 */

public class Constants {



    public static final String METHOD_AUNTHENTICATION ="Authenticate";

    public static final String METHOD_GETCITYFROM ="GetFromCities";

    public static final String METHOD_GETCITYTO ="GetToCities";

    public static final String METHOD_GET_ROUTES ="GetDetailedRoutes";
    public static final String METHOD_GET_ROUTES1 = "GetRoutes2";

    public static final String GLOBEL_URL = "http://affapi.mantistechnologies.com/service.asmx";

    public static final String GLOBEL_NAMESPACE = "http://tempuri.org/";

    public static final String METHOD_GetRouteScheduleDetail = "GetRouteScheduleDetailsWithComm";

    public static final String METHOD_HoldSeatsForSchedule = "HoldSeatsForSchedule";

    public static final String METHOD_BookSeats = "BookSeats";

    public static final String METHOD_ISCANCELABLE ="IsCancellable2";

    public static final String METHOD_CANCEL_TICKET = "CancelTicket2";

    public static final String METHOD_TICKET_DETAIL = "GetBookingInfo";

    public static final int MaxSeats = 5;

    public static final int Radio_mr = 1000;
    public static final int Radio_mrs = 2000;
    public static final int Radio_ms = 3000;

    public static final String Send_mail_url = "http://xercesblue.website/upload/testmail.php";
    public static final String Get_offer_global = "http://xercesblue.website/iservice/client_requests/promocodes/get_all_offers";
    public static final String get_offer_with_coupan_no = "http://xercesblue.website/iservice/client_requests/promocodes/get_offer_with_coupan_no";
    public static final String Add_transaction = "http://xercesblue.website/iservice/client_requests/transaction/add_transaction";
    public static final String Success_payment = "http://xercesblue.website/iservice/client_requests/transaction/update_success_payment";
    public static final String Start_booking = "http://xercesblue.website/iservice/client_requests/transaction/update_start_booking_ticket";
    public static final String Success_booking = "http://xercesblue.website/iservice/client_requests/transaction/update_success_booking";

    public static final String Login_url = "http://xercesblue.website/iservice/client_requests/treval_user/loginauth";
    public static final String Signup_url = "http://xercesblue.website/iservice/client_requests/treval_user/add_user";

    public static final String Get_booked_ticket = "http://xercesblue.website/iservice/client_requests/transaction/get_booked_ticket";
    public static final String Get_booked_ticket_detail = "http://xercesblue.website/iservice/client_requests/transaction/get_booked_ticket_detail";

    public static final String update_ticket_status ="http://xercesblue.website/iservice/client_requests/transaction/update_ticket_status";
    public static final String enable_promocode = "http://xercesblue.website/iservice/client_requests/promocodes/enable_coupan";
    public static final String refund_amount = "http://xercesblue.website/iservice/client_requests/transaction/cancel_ticket_refund";
    public static final String get_extra_charge ="http://xercesblue.website/iservice/client_requests/promocodes/get_extra_charge";

    public static final int state_main_cities = 0;
    public static final int state_all_cities = 1;
}

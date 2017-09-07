package com.iseva.app.source.travel;

/**
 * Created by xb_sushil on 1/20/2017.
 */

public class Constants {


    public static final int MAX_SEATS = 5;

    public static final int RADIO_MR = 1000;
    public static final int RADIO_MRS = 2000;
    public static final int RADIO_MS = 3000;





    public static final int STATE_MAIN_CITIES = 0;
    public static final int STATE_ALL_CITIES = 1;


    public class JSON_KEYS {


        public static final String SUCCESS = "success";
        public static final String DATA = "data";
        public static final String CITY_ID = "CityId";
        public static final String CITY_NAME = "City";

    }


    public static class URL_TY {


        // NEW API
        public static String BASE_URL = "http://api.iamgds.com/ota/";

        public static String BASE_TRANSACTION_URL = "http://tranapi.iamgds.com/ota/";

        public static final String GET_CITY_LIST = BASE_URL + "CityList";

        public static final String SEARCH_BUSES = BASE_URL + "Search";

        public static final String GET_CHART = BASE_URL + "Chart";

        //TRANSACTION APIS

        public static final String HOLD_SEATS = BASE_TRANSACTION_URL + "HoldSeats";


        public static final String BOOK_SEATS = BASE_TRANSACTION_URL + "BookSeats";

        public static final String GET_IS_CANCELLABLE = BASE_TRANSACTION_URL + "IsCancellable";

        public static final String GET_BOOKING_DETAILS = BASE_TRANSACTION_URL + "BookingDetails";

        public static final String CANCEL_BOOKED_TICKET = BASE_TRANSACTION_URL + "CancelSeats";

    }
    public class URL_XB {


        public static final String BASE_URL = "http://xercesblue.website/iservice/";

        public static final String CLIENT_REQ_URL = BASE_URL + "client_requests/";

        public static final String SEND_MAIL_URL = BASE_URL + "testmail.php";
        public static final String GET_OFFER_GLOBAL = CLIENT_REQ_URL + "promocodes/get_all_offers";
        public static final String GET_OFFER_WITH_COUPON_NO = CLIENT_REQ_URL + "promocodes/get_offer_with_coupan_no";
        public static final String ADD_TRANSACTION = CLIENT_REQ_URL + "transaction/add_transaction";
        public static final String SUCCESS_PAYMENT = CLIENT_REQ_URL + "transaction/update_success_payment";
        public static final String START_BOOKING = CLIENT_REQ_URL + "transaction/update_start_booking_ticket";
        public static final String SUCCESS_BOOKING = CLIENT_REQ_URL + "transaction/update_success_booking";

        public static final String LOGIN = CLIENT_REQ_URL + "treval_user/loginauth";
        public static final String SIGNUP = CLIENT_REQ_URL + "treval_user/add_user";

        public static final String GET_BOOKED_TICKET = CLIENT_REQ_URL + "transaction/get_booked_ticket";
        public static final String GET_BOOKED_TICKET_DETAILS = CLIENT_REQ_URL + "transaction/get_booked_ticket_detail";

        public static final String UPDATE_TICKET_STATUS = CLIENT_REQ_URL + "transaction/update_ticket_status";
        public static final String ENABLE_PROMOCODE = CLIENT_REQ_URL + "promocodes/enable_coupan";
        public static final String REFUND_AMOUNT = CLIENT_REQ_URL + "transaction/cancel_ticket_refund";
        public static final String GET_EXTRA_CHARGE = CLIENT_REQ_URL + "promocodes/get_extra_charge";
        public static final String GET_COMMITION_EXTRA_CHARGE = CLIENT_REQ_URL + "promocodes/get_commition_extra_charge";
        public static final String SEND_MESSAGE_URL = CLIENT_REQ_URL + "transaction/send_message";
        public static final String GET_PROMO_IMAGES = CLIENT_REQ_URL + "promocodes/get_promo_images";
        public static final String GET_DEFAULT_TRAVEL_DATA = CLIENT_REQ_URL + "promocodes/get_default_travel_data";


    }

    public class BUS_TYPE {


        public static final String AC = "AC";
        public static final String NON_AC = "NON_AC";


        public static final String SEATER= "SEATER";
        public static final String SEATER_SEMI_SLEEPER = "SEATER_SEMI_SLEEPER";
        public static final String SLEEPER = "SLEEPER";
        public static final String SEATER_SLEEPER = "SEATER_SLEEPER";
        public static final String SEMI_SLEEPER = "SEMI_SLEEPER";
        public static final String SEMI_SLEEPER_SLEEPER = "SEMI_SLEEPER_SLEEPER";






        public static final String MAKE_MERCEDES = "MERCEDES";
        public static final String MAKE_NORMAL = "NORMAL";
        public static final String MAKE_VOLVO = "VOLVO";
        public static final String MAKE_VOLVO_ISHIFT = "VOLVO_ISHIFT";
        public static final String MAKE_SCANIA = "SCANIA";



        public static final String SINGLE_AXLE = "SINGLE_AXLE";
        public static final String MULTI_AXLE = "MULTI_AXLE";




    }

    public class SEAT_DETAILS{

        public static final int VALUE_DECK_1 = 1;
        public static final int VALUE_DECK_2 = 2;

        public static final int VALUE_SEAT_TYPE_SEATING = 1;
        public static final int VALUE_SEAT_TYPE_SLEEPER = 2;
        public static final int VALUE_SEAT_TYPE_SEMI_SLEEPER = 4;


        public static final int VALUE_SEAT_STATUS_NOT_AVL = 0;
        public static final int VALUE_SEAT_STATUS_AVL_FOR_ALL = 1;
        public static final int VALUE_SEAT_STATUS_AVL_FOR_MALE = 2;
        public static final int VALUE_SEAT_STATUS_AVL_FOR_FEMALE = 3;
        public static final int VALUE_SEAT_STATUS_BOOKED_BY_MALE = -2;
        public static final int VALUE_SEAT_STATUS_BOOKED_BY_FEMALE = -3;

        public static final int INDEX_SEAT_LAYOUT_SEQ_NO = 0;
        public static final int INDEX_SEAT_LAYOUT_ROW = 1;
        public static final int INDEX_SEAT_LAYOUT_COL = 2;
        public static final int INDEX_SEAT_LAYOUT_WIDTH = 3;
        public static final int INDEX_SEAT_LAYOUT_HEIGHT = 4;
        public static final int INDEX_SEAT_LAYOUT_SEAT_TYPE = 5;

        public static final int INDEX_FARE_TOTAL = 0;
        public static final int INDEX_FARE_BASE = 1;


        public static final int VALUE_GENDER_MALE = 0;
        public static final int VALUE_GENDER_FEMALE = 1;
        public static final int VALUE_GENDER_ALL= 2;

    }

}

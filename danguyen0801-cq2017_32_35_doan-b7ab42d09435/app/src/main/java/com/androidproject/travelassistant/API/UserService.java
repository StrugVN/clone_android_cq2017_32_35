package com.androidproject.travelassistant.API;

import com.androidproject.travelassistant.AppData.UserCoord;
import com.androidproject.travelassistant.Request.AddCommentRequest;
import com.androidproject.travelassistant.Request.AddCommentResponse;
import com.androidproject.travelassistant.Request.AddMemberRequest;
import com.androidproject.travelassistant.Request.AddMemberResponse;
import com.androidproject.travelassistant.Request.AddNotiOnRoadRequest;
import com.androidproject.travelassistant.Request.AddNotiOnRoadRespone;
import com.androidproject.travelassistant.Request.AddStopPointRequest;
import com.androidproject.travelassistant.Request.AddStopPointResponse;
import com.androidproject.travelassistant.Request.CloneATourRequest;
import com.androidproject.travelassistant.Request.CloneATourResponse;
import com.androidproject.travelassistant.Request.CreateTourRequest;
import com.androidproject.travelassistant.Request.CreateTourResponse;
import com.androidproject.travelassistant.Request.GetCommentListResponse;
import com.androidproject.travelassistant.Request.GetCoordinateUsersRequest;
import com.androidproject.travelassistant.Request.GetCoordinateUsersResponse;
import com.androidproject.travelassistant.Request.GetHistoryDestinationRequest;
import com.androidproject.travelassistant.Request.GetHistoryDestinationResponse;
import com.androidproject.travelassistant.Request.GetHistoryTourByStatusResponse;
import com.androidproject.travelassistant.Request.GetHistoryTourResponse;
import com.androidproject.travelassistant.Request.GetInvitationUsersResponse;
import com.androidproject.travelassistant.Request.GetListFeedbackResponse;
import com.androidproject.travelassistant.Request.GetListReviewResponse;
import com.androidproject.travelassistant.Request.GetNotificationOnRoadResponse;
import com.androidproject.travelassistant.Request.GetNotificationCoordinateResponse;
import com.androidproject.travelassistant.Request.GetNotificationListResponse;
import com.androidproject.travelassistant.Request.GetPointFeedbackResponse;
import com.androidproject.travelassistant.Request.GetPointReviewResponse;
import com.androidproject.travelassistant.Request.GetRestaurantResponse;
import com.androidproject.travelassistant.Request.GetServiceDetailResponse;
import com.androidproject.travelassistant.Request.GetSuggestedDestinationFromPointRequest;
import com.androidproject.travelassistant.Request.GetSuggestedDestinationRequest;
import com.androidproject.travelassistant.Request.GetSuggestedDestinationResponse;
import com.androidproject.travelassistant.Request.GetTourInfoResponse;
import com.androidproject.travelassistant.Request.GetTourListResponse;
import com.androidproject.travelassistant.Request.GetUserInfoResponse;
import com.androidproject.travelassistant.Request.OTPPasswordRecoveryRequest;
import com.androidproject.travelassistant.Request.OTPPasswordRecoveryResponse;
import com.androidproject.travelassistant.Request.RemoveStopPointResponse;
import com.androidproject.travelassistant.Request.ReportFeedbackRequest;
import com.androidproject.travelassistant.Request.ReportFeedbackResponse;
import com.androidproject.travelassistant.Request.ReportReviewResponse;
import com.androidproject.travelassistant.Request.ResponseInvitationRequest;
import com.androidproject.travelassistant.Request.ResponseInvitationResponse;
import com.androidproject.travelassistant.Request.SearchHistoryUserResponse;
import com.androidproject.travelassistant.Request.SearchServiceResponse;
import com.androidproject.travelassistant.Request.SearchUserResponse;
import com.androidproject.travelassistant.Request.SendFeedbackRequest;
import com.androidproject.travelassistant.Request.SendFeedbackResponse;
import com.androidproject.travelassistant.Request.SendReviewRequest;
import com.androidproject.travelassistant.Request.SendReviewResponse;
import com.androidproject.travelassistant.Request.SendTextNotiResponse;
import com.androidproject.travelassistant.Request.SendTextNotificationRequest;
import com.androidproject.travelassistant.Request.ThirdPartyLoginRequest;
import com.androidproject.travelassistant.Request.ThirdPartyLoginResponse;
import com.androidproject.travelassistant.Request.LoginRequest;
import com.androidproject.travelassistant.Request.LoginResponse;
import com.androidproject.travelassistant.Request.SignUpRequest;
import com.androidproject.travelassistant.Request.SignUpResponse;
import com.androidproject.travelassistant.Request.UpdateAvatarServiceRequest;
import com.androidproject.travelassistant.Request.UpdateAvatarServiceResponse;
import com.androidproject.travelassistant.Request.UpdateAvatarTourRequest;
import com.androidproject.travelassistant.Request.UpdateAvatarTourResponse;
import com.androidproject.travelassistant.Request.UpdateLandingTimesRequest;
import com.androidproject.travelassistant.Request.UpdateLandingTimesResponse;
import com.androidproject.travelassistant.Request.UpdatePasswordRequest;
import com.androidproject.travelassistant.Request.UpdatePasswordResponse;
import com.androidproject.travelassistant.Request.UpdateStatusTourRequest;
import com.androidproject.travelassistant.Request.UpdateStatusTourResponse;
import com.androidproject.travelassistant.Request.UpdateStopPointRequest;
import com.androidproject.travelassistant.Request.UpdateStopPointResponse;
import com.androidproject.travelassistant.Request.UpdateTourRequest;
import com.androidproject.travelassistant.Request.UpdateTourResponse;
import com.androidproject.travelassistant.Request.UpdateUserAvatarRequest;
import com.androidproject.travelassistant.Request.UpdateUserAvatarResponse;
import com.androidproject.travelassistant.Request.UpdateUserInfoRequest;
import com.androidproject.travelassistant.Request.UpdateUserInfoResponse;
import com.androidproject.travelassistant.Request.VerifyCodeEmailVerificationResponse;
import com.androidproject.travelassistant.Request.VerifyEmailResponse;
import com.androidproject.travelassistant.Request.VerifyOTPPasswordRecoveryRequest;
import com.androidproject.travelassistant.Request.VerifyOTPPasswordRecoveryResponse;

import org.json.JSONArray;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserService {
    @POST("/user/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("/user/register")
    Call<SignUpResponse> sign_up(@Body SignUpRequest request);

    @POST("/user/login/by-google")
    Call<ThirdPartyLoginResponse> google_login(@Body ThirdPartyLoginRequest request);

    @POST("/user/login/by-facebook")
    Call<ThirdPartyLoginResponse> facebook_login(@Body ThirdPartyLoginRequest request);


    @GET("/tour/list")
    Call<GetTourListResponse> get_tour(@Header("Authorization") String token,
                                       @Query("rowPerPage") int row,
                                       @Query("pageNum") int pageNum);

    @GET("/tour/list")
    Call<GetTourListResponse> get_tour(@Header("Authorization") String token,
                                       @Query("rowPerPage") int row,
                                       @Query("pageNum") int pageNum,
                                       @Query("orderBy") String order);

    @GET("/tour/list")
    Call<GetTourListResponse> get_tour(@Header("Authorization") String token,
                                       @Query("rowPerPage") int row,
                                       @Query("pageNum") int pageNum,
                                       @Query("isDesc") boolean isDesc);

    @GET("/tour/list")
    Call<GetTourListResponse> get_tour(@Header("Authorization") String token,
                                       @Query("rowPerPage") int row,
                                       @Query("pageNum") int pageNum,
                                       @Query("orderBy") String order,
                                       @Query("isDesc") boolean isDesc);

    @POST("/tour/create")
    Call<CreateTourResponse> create_tour(@Header("Authorization") String token,
                                         @Body CreateTourRequest request);

    @GET("/user/info")
    Call<GetUserInfoResponse> get_user(@Header("Authorization") String token);

    @POST("/user/update-avatar")
    Call<UpdateUserAvatarResponse> update_avatar(@Header("Authorization") String token,
                                                 @Body UpdateUserAvatarRequest request);

    @POST("/user/update-password")
    Call<UpdatePasswordResponse> update_password(@Header("Authorization") String token,
                                                 @Body UpdatePasswordRequest request);

    @POST("/user/edit-info")
    Call<UpdateUserInfoResponse> update_UserInfo(@Header("Authorization") String token,
                                                 @Body UpdateUserInfoRequest request);

    @POST("/user/request-otp-recovery")
    Call<OTPPasswordRecoveryResponse> OTP_Password(@Header("Authorization") String token,
                                                   @Body OTPPasswordRecoveryRequest request);

    @POST("/user/verify-otp-recovery")
    Call<VerifyOTPPasswordRecoveryResponse> Verify_OTP_Password(@Body VerifyOTPPasswordRecoveryRequest request);

    @GET("/user/send-active")
    Call<VerifyEmailResponse> Verify_Email(@Header("Authorization") String token,
                                           @Query("userId") int userId,
                                           @Query("type") String type);

    @GET("/user/active")
    Call<VerifyCodeEmailVerificationResponse> Verify_Code_Email(@Header("Authorization") String token);



    @GET("/user/search")
    Call <SearchUserResponse> get_search_user (@Query("searchKey") String searchKey,
                                               @Query("pageIndex") int pageIndex,
                                               @Query("pageSize") String pageSize);


    @POST("/tour/set-stop-points")
    Call<AddStopPointResponse> Add_Stop_Point(@Header("Authorization") String token,
                                              @Body AddStopPointRequest request);

    @POST("/tour/clone")
    Call<CloneATourResponse> Clone_A_Tour(@Header("Authorization") String token,
                                          @Body CloneATourRequest request);

    @GET("/tour/comment-list")
    Call<GetCommentListResponse> Get_Comment(@Header("Authorization") String token,
                                             @Query("tourId") String tourId,
                                             @Query("pageIndex") int pageIndex,
                                             @Query("pageSize") String pageSize);

    @POST("/tour/get/destination-history-user")
    Call<GetHistoryDestinationResponse> Get_Histoy_Destination(@Header("Authorization") String token,
                                                               @Body GetHistoryDestinationRequest request);

    @GET("/tour/history-user-by-status")
    Call<GetHistoryTourByStatusResponse> Get_History_By_Status(@Header("Authorization") String token);

    @GET("/tour/history-user")
    Call<GetHistoryTourResponse> Get_History(@Header("Authorization") String token,
                                             @Query("pageIndex") int pageIndex,
                                             @Query("pageSize") int pageSize);

    @GET("/tour/get/feedback-service")
    Call<GetListFeedbackResponse> Get_Feedbacks(@Header("Authorization") String token,
                                                @Query("serviceId") int serviced,
                                                @Query("pageIndex") int pageIndex,
                                                @Query("pageSize") String pageSize);

    @GET("/tour/get/review-list")
    Call<GetListReviewResponse> Get_Review(@Header("Authorization") String token,
                                           @Query("tourId") int tourId,
                                           @Query("pageIndex") int pageIndex,
                                           @Query("pageSize") String pageSize);

    @POST("/tour/notification")
    Call<SendTextNotiResponse> Send_Text_Noti(@Header("Authorization") String token,
                                              @Body SendTextNotificationRequest request);

    @GET("/tour/notification-list")
    Call<GetNotificationListResponse> Get_Notification_List(@Header("Authorization") String token,
                                                            @Query("tourId") int tourId,
                                                            @Query("pageIndex") int pageIndex,
                                                            @Query("pageSize") String pageSize);


    @GET("/tour/get/noti-on-road-coordinate")
    Call<GetNotificationCoordinateResponse> Get_Notification_Coordinate(@Header("Authorization") String token,
                                                                        @Query("lat") int lat,
                                                                        @Query("longg") int longg,
                                                                        @Query("notificationType") int notificationType);

    @GET("/tour/get/noti-on-road")
    Call<GetNotificationOnRoadResponse> Get_Notication_OnRoad_TourId (@Header("Authorization") String token,
                                                               @Query("tourId") int tourId,
                                                               @Query("pageIndex") int pageIndex,
                                                               @Query("pageSize") String pageSize);

    @GET("/tour/get/feedback-point-stats")
    Call <GetPointFeedbackResponse> Get_Point_Feedback (@Header ("Authorization") String token,
                                                        @Query("serviceId") int serviceId);
    @GET("/tour/get/review-point-stats")
    Call <GetPointReviewResponse> Get_Point_Review (@Header ("Authorization") String token,
                                                      @Query("tourId") int tourId);

    @GET("/tour/get/restaurant-menu")
    Call <GetRestaurantResponse> Get_Restaurant (@Header ("Authorization")String token,
                                                 @Query("serviceId") int serviceId);

    @POST("/tour/current-users-coordinate")
    Call <ArrayList<UserCoord>> Get_Coordinate_Users (@Header ("Authorization") String token,
                                                      @Body GetCoordinateUsersRequest request);

    @GET("/tour/get/invitation")
    Call<GetInvitationUsersResponse> Get_Invitation (@Header("Authorization") String token,
                                                     @Query("pageIndex") int pageIndex,
                                                     @Query("pageSize") String pageSize);
    @GET("/tour/remove-stop-point")
    Call <RemoveStopPointResponse> Get_Remove_Stop_Point (@Header("Authorization") String token,
                                                          @Query("stopPointId") String stopPointId);
    @POST("/tour/report/feedback")
    Call <ReportFeedbackResponse> Report_Feedback (@Header ("Authurization") String token,
                                                   @Body ReportFeedbackRequest request);

    @POST("/tour/report/review")
    Call <ReportReviewResponse> Report_Review (@Header ("Authurization") String token,
                                                 @Body ReportFeedbackRequest request);

    @GET("/tour/search/service")
    Call <SearchServiceResponse> Get_Search_Service (@Header("Authorization") String token,
                                                     @Query("searchKey") String searchKey,
                                                     @Query("pageIndex") int pageIndex,
                                                     @Query("pageSize") int pageSize);

    @GET("/tour/search-history-user")
    Call<SearchHistoryUserResponse> Get_Search_History_User (@Header("Authorization") String token,
                                                           @Query("searchKey") String searchKey,
                                                           @Query("pageIndex") int pageIndex,
                                                           @Query("pageSize") int pageSize);

    @POST("/tour/add/feedback-service")
    Call <SendFeedbackResponse> Send_Feedback (@Header("Authorization") String token,
                                               @Body SendFeedbackRequest request);
    @POST("/tour/add/review")
    Call <SendReviewResponse> Send_Review (@Header("Authorization") String token,
                                           @Body SendReviewRequest request);


    @POST("/tour/update-stop-point")
    Call <UpdateStopPointResponse> Update_Stop_Point (@Header("Authorization") String token,
                                                      @Body UpdateStopPointRequest request);


    @POST("/tour/update-tour")
    Call <UpdateTourResponse> Update_Tour (@Header("Authorization") String token,
                                           @Body UpdateTourRequest request);
    @POST("/tour/update/avatar-for-service")
    Call <UpdateAvatarServiceResponse> Update_Avatar_Service (@Header("Authorization") String token,
                                                              @Body UpdateAvatarServiceRequest request);

    @POST("/tour/update/avatar-for-tour")
    Call <UpdateAvatarTourResponse> Update_Avatar_Tour (@Header("Authorization") String token,
                                                        @Body UpdateAvatarTourRequest request);

    @POST("/tour/update/landing-times-for-destination")
    Call <UpdateLandingTimesResponse> Update_Avatar_Tour (@Header("Authorization") String token,
                                                          @Body UpdateLandingTimesRequest request);

    @POST("/tour/finish-trip")
    Call <UpdateStatusTourResponse> Update_Status_Tour (@Header("Authorization") String token,
                                                        @Body UpdateStatusTourRequest request);

    @POST("/tour/response/invitation")
    Call <ResponseInvitationResponse> Response_Invitation (@Header("Authorization") String token,
                                                          @Body ResponseInvitationRequest request);

    @GET("/tour/info")
    Call<GetTourInfoResponse> Get_Tour_Info(@Header("Authorization") String token,
                                            @Query("tourId") int id);

    @POST("/tour/comment")
    Call<AddCommentResponse> Add_Comment(@Header("Authorization") String token,
                                         @Body AddCommentRequest request);

    @POST("/tour/suggested-destination-list")
    Call<GetSuggestedDestinationResponse> get_suggested_destination_from_point(@Header("Authorization") String token,
                                                                               @Body GetSuggestedDestinationFromPointRequest request);

    @POST("/tour/suggested-destination-list")
    Call<GetSuggestedDestinationResponse> get_suggested_destination(@Header("Authorization") String token,
                                                                               @Body GetSuggestedDestinationRequest request);

    @POST("/tour/add/member")
    Call<AddMemberResponse> Add_Member(@Header("Authorization") String token,
                                       @Body AddMemberRequest request);

    @GET("/tour/get/service-detail")
    Call<GetServiceDetailResponse> Get_Service_Detail(@Header("Authorization") String token,
                                                      @Query("serviceId") int serviceId);

    @POST("/tour/add/notification-on-road")
    Call<AddNotiOnRoadRespone> Add_Noti_On_Road(@Header("Authorization") String token,
                                                @Body AddNotiOnRoadRequest request);
}
// /**
// * 
// */
// package com.shoes.webshoes.controller;

// import java.io.IOException;
// import java.text.ParseException;
// import java.text.SimpleDateFormat;
// import java.time.Duration;
// import java.util.ArrayList;
// import java.util.Date;
// import java.util.HashSet;
// import java.util.List;
// import java.util.Set;
// import java.util.concurrent.Callable;
// import java.util.stream.Collectors;

// import javax.servlet.http.HttpServletRequest;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.support.DefaultMessageSourceResolvable;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.MethodArgumentNotValidException;
// import org.springframework.web.bind.MissingServletRequestParameterException;
// import org.springframework.web.bind.annotation.ExceptionHandler;
// import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.client.RestTemplate;
// import org.springframework.web.context.request.RequestContextHolder;
// import org.springframework.web.context.request.ServletRequestAttributes;
// import org.springframework.web.context.request.WebRequest;

// import com.english_center.common.enums.VideoTypeEnum;
// import com.english_center.dao.ChapterDao;
// import com.english_center.dao.LessonsDao;
// import com.english_center.entity.Chapter;
// import com.english_center.entity.Exam;
// import com.english_center.entity.Lessons;
// import com.english_center.entity.Result;
// import com.english_center.entity.UserCourseProgress;
// import com.english_center.entity.Users;
// import com.english_center.model.YouTubeVideo;
// import com.english_center.model.YouTubeVideoListResponse;
// import com.english_center.response.BaseResponse;
// import com.english_center.response.ExamResponse;
// import com.english_center.security.ApplicationProperties;
// import com.english_center.security.JwtTokenUtil;
// import com.english_center.service.AudioService;
// import com.english_center.service.CategoryCourseService;
// import com.english_center.service.CategoryExamService;
// import com.english_center.service.ChapterService;
// import com.english_center.service.CityService;
// import com.english_center.service.ClassStudentService;
// import com.english_center.service.CommentsService;
// import com.english_center.service.CourseService;
// import com.english_center.service.DistrictService;
// import com.english_center.service.ExamService;
// import com.english_center.service.IFirebaseImageService;
// import com.english_center.service.ImageService;
// import com.english_center.service.LessonsService;
// import com.english_center.service.NoteService;
// import com.english_center.service.PaymentService;
// import com.english_center.service.QuestionService;
// import com.english_center.service.ReplyCommentsService;
// import com.english_center.service.ResultDetailService;
// import com.english_center.service.ResultService;
// import com.english_center.service.StatisticalService;
// import com.english_center.service.TopicExamService;
// import com.english_center.service.UserCourseProgressService;
// import com.english_center.service.UserCourseService;
// import com.english_center.service.UserRegisterService;
// import com.english_center.service.UserService;
// import com.english_center.service.VideoWatchHistoryService;
// import com.english_center.service.WardsService;
// import com.english_center.service.impl.JwtUserDetailsService;
// import com.english_center.service.impl.SendEmail;
// import com.google.api.services.drive.Drive;
// import com.google.api.services.drive.model.File;
// import com.google.api.services.drive.model.Permission;

// @RestController
// public class BaseController {
// 	@Autowired
// 	public NoteService noteService;

// 	@Autowired
// 	public JwtTokenUtil jwtTokenUtil;

// 	@Autowired
// 	public JwtUserDetailsService userDetailsService;

// 	@Autowired
// 	public SendEmail sendEmail;

// 	@Autowired
// 	public CityService cityService;

// 	@Autowired
// 	public DistrictService districtService;

// 	@Autowired
// 	public WardsService wardsService;

// 	@Autowired
// 	public UserRegisterService userRegisterService;

// 	@Autowired
// 	public StatisticalService statisticalService;

// 	@Autowired
// 	public TopicExamService topicExamService;

// 	@Autowired
// 	public AudioService audioService;

// 	@Autowired
// 	public CategoryExamService categoryExamService;

// 	@Autowired
// 	public ChapterService chapterService;

// 	@Autowired
// 	public LessonsService lessonsService;

// 	@Autowired
// 	public CategoryCourseService categoryCourseService;

// 	@Autowired
// 	public VideoWatchHistoryService videoWatchHistoryService;

// 	@Autowired
// 	public CourseService courseService;

// 	@Autowired
// 	public PaymentService paymentService;

// 	@Autowired
// 	LessonsDao lessonsDao;

// 	@Autowired
// 	ChapterDao chapterDao;

// 	@Autowired
// 	public UserService userService;

// 	@Autowired
// 	public ClassStudentService classStudentService;

// 	@Autowired
// 	public UserCourseService userCourseService;

// 	@Autowired
// 	public ImageService imageService;

// 	@Autowired
// 	public ResultService resultService;

// 	@Autowired
// 	public IFirebaseImageService iFirebaseImageService;

// 	@Autowired
// 	public CommentsService commentsService;

// 	@Autowired
// 	public ReplyCommentsService replyCommentsService;

// 	@Autowired
// 	public UserCourseProgressService userCourseProgressService;

// 	@Autowired
// 	public QuestionService questionService;

// 	@Autowired
// 	public ExamService examService;

// 	@Autowired
// 	public ResultDetailService resultDetailService;

// 	@Autowired
// 	public Drive googleDrive;

// 	// call api google
// 	@Autowired
// 	RestTemplate restTemplate;

// 	@Autowired
// 	public ApplicationProperties applicationProperties;

// 	// time 3 phút
// 	public static final long TIME_OTP_EXPIRED = 1000 * 60 * 3;

// 	@SuppressWarnings({ "rawtypes", "unchecked" })
// 	@ExceptionHandler(MethodArgumentNotValidException.class)
// 	public final ResponseEntity<BaseResponse> handleUserNotFoundException(MethodArgumentNotValidException ex,
// 			WebRequest request) {

// 		BaseResponse response = new BaseResponse();
// 		response.setStatus(HttpStatus.BAD_REQUEST);
// 		response.setMessageError("Dữ liệu không hợp lệ");

// 		List<String> errors = ex.getBindingResult().getFieldErrors().stream()
// 				.map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());

// 		response.setData(errors);
// 		return new ResponseEntity<>(response, HttpStatus.OK);
// 	}

// 	@SuppressWarnings({ "rawtypes", "unchecked" })
// 	@ExceptionHandler(MissingServletRequestParameterException.class)
// 	public ResponseEntity<BaseResponse> handleMissingParams(MissingServletRequestParameterException ex) {
// 		// Actual exception handling
// 		BaseResponse response = new BaseResponse();
// 		response.setStatus(HttpStatus.BAD_REQUEST);
// 		response.setMessageError(String.format("%s is required!", ex.getParameterName()));
// 		response.setData(null);

// 		return new ResponseEntity<>(response, HttpStatus.OK);
// 	}

// 	public String formatDate(String inputDate) throws ParseException {
// 		SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd/MM/yyyy");
// 		Date inputDate1 = inputDateFormat.parse(inputDate);

// 		SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
// 		return outputDateFormat.format(inputDate1);

// 	}

// 	public int countUserExam(int examId) {
// 		List<Result> results = getListWithExceptionHandler(() -> resultService.findByExamId(examId));

// 		List<Integer> listId = new ArrayList<>();
// 		results.stream().forEach(x -> listId.add(x.getUserId()));

// 		Set<Integer> uniqueUserIds = new HashSet<>();

// 		listId.stream().forEach(uniqueUserIds::add);

// 		return uniqueUserIds.size();
// 	}

// 	public int countComment(int examId) throws Exception {

// 		return commentsService.countComments(examId);
// 	}

// 	public long caculateOtpExpired(Date otpDate) {
// 		Date currentDate = new Date();
// 		return currentDate.getTime() - (otpDate.getTime() + TIME_OTP_EXPIRED);

// 	}

// 	public HttpServletRequest getRequest() {
// 		return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
// 	}

// 	public String getRequestHeaderAccessToken() {
// 		String authorizeHeader = this.getRequest().getHeader("Authorization");
// 		return authorizeHeader.replace("Bearer ", "");
// 	}

// 	@SuppressWarnings("unused")
// 	public Users getUser() throws Exception {

// 		String username = null;
// 		String jwtToken = null;

// 		jwtToken = this.getRequestHeaderAccessToken();
// 		username = jwtTokenUtil.getUsernameFromToken(jwtToken);

// 		Users user = userService.findUsersByUsersName(username);

// 		if (user.getIsLogin() == 0 && user.getAccessToken() == "")
// 			throw new Exception("Tài khoản chưa đăng nhập");
// 		if (user != null)
// 			return user;
// 		else
// 			throw new Exception("Thất bại");
//     }
// 	public static <T> List<T> getListWithExceptionHandler(Callable<List<T>> callable) {
// 		try {
// 			return callable.call();
// 		} catch (Exception e) {
// 			e.printStackTrace();
// 			return new ArrayList<>();
// 		}
// 	}

// 	@SuppressWarnings("unchecked")
// 	public static <T> T getOneWithExceptionHandler(Callable<T> callable) {
// 		try {
// 			return callable.call();
// 		} catch (Exception e) {
// 			e.printStackTrace();
// 			return (T) new Object();
// 		}
// 	}

// }

package stdio.kiteDream.module.feedback.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import stdio.kiteDream.module.feedback.bean.Feedback;
import stdio.kiteDream.module.feedback.service.FeedbackService;
import stdio.kiteDream.module.vo.JsonVO;
import stdio.kiteDream.util.Constant;

@Controller
@RequestMapping("/api/feedback")
public class FeedbackController {

	@Autowired
	private FeedbackService feedbackService;

	@ResponseBody
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	public JsonVO list(ModelMap model, @RequestParam("pageNo")int pageNo,  @RequestParam("pageSize")int pageSize) {
		JsonVO json = new JsonVO();
		try {
			json.setResult(feedbackService.getFeedbacks(pageNo, pageSize));
			json.setErrorcode(Constant.OK);
		} catch (Exception e) {
			e.printStackTrace();
			json.setErrorcode(Constant.FAIL);
		}
		return json;
	}

	@ResponseBody
	@RequestMapping(value = "/add",  method = { RequestMethod.GET, RequestMethod.POST })
	public JsonVO add(ModelMap model, @RequestParam("title") String title,
			@RequestParam("info") String info) {
		JsonVO json = new JsonVO();
		try {
			Feedback feedback = new Feedback();
			feedback.setInfo(info);
			feedback.setTitle(title);
			if(feedbackService.saveFeedback(feedback)){
				json.setErrorcode(Constant.OK);
			}
		} catch (Exception e) {
			json.setErrorcode(Constant.FAIL);
			e.printStackTrace();
		}
		return json;
	}

	@ResponseBody
	@RequestMapping(value = "/del/{id}", method = { RequestMethod.GET, RequestMethod.POST })
	public JsonVO del(ModelMap model, @PathVariable("id") String id) {
		JsonVO json = new JsonVO();
		try {
			if(feedbackService.deleteFeedback(id)){
				json.setErrorcode(Constant.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.setErrorcode(Constant.FAIL);
		}
		return json;
	}

}
package com.custom.controllers;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.custom.enties.VideoRequest;
import com.custom.enties.img;
import com.custom.enties.rites;
import com.custom.respository.RespoVideo;
import com.custom.respository.imgRespo;
import com.custom.respository.respo;

import jakarta.servlet.http.HttpSession;

@RestController
public class controller {

	@Autowired
	respo respo;
	
	@Autowired
	RespoVideo respoVideo;

	@Autowired
	imgRespo imgRespo;

	
	
	//request video editing http://localhost:1001/videoRequest
	
	@PostMapping("/videoRequest")
	public String submits(@ModelAttribute VideoRequest op) {
		
		respoVideo.save(op);
		return "as";
	}
	
	//request video editing http://localhost:1001/Register
	
	@PostMapping("/Register")
	public rites register(@RequestBody rites rite) {
		rites rites = new rites();

		rites.setName(rite.getName());
		rites.setEmail(rite.getEmail());
		rites.setPhoneNo(rite.getPhoneNo());
		rites.setPassword(rite.getPassword());
		rites.setAdmin(false);
		rites ops= respo.save(rites);
		return ops;
	}
	
	
	//request video editing http://localhost:1001/addAdmin
	
	@PostMapping("/addAdmin")
	public rites addAdmin(@RequestParam("name") String name, @RequestParam("email") String email,
			@RequestParam("phoneNo") String phoneNo, @RequestParam("password") String password) {
		rites rites = new rites();

		rites.setName(name);
		rites.setEmail(email);
		rites.setPhoneNo(phoneNo);
		rites.setPassword(password);
		rites.setAdmin(true);

		rites ops=	respo.save(rites);
		return ops;
	}
	
	
	//request video editing http://localhost:1001/videoUpload

	@PostMapping(value = "/videoUpload")
	public img videoSave(@RequestParam("thumbnail") MultipartFile thumbnail,
			@RequestParam("video") MultipartFile video, @RequestParam("name") String name, HttpSession s) {

		System.out.println(thumbnail.getOriginalFilename());
		String thumbnails = thumbnail.getOriginalFilename();
		String videos = video.getOriginalFilename();

		img img = new img();
		img.setThumbnails(thumbnails);
		img.setName(name);
		img.setVideos(videos);

		img ops= imgRespo.save(img);

		try {
			// video
			byte[] b = thumbnail.getBytes();
			String path = s.getServletContext().getRealPath("images/") + thumbnail.getOriginalFilename();
			System.out.println(path);
			FileOutputStream fos = new FileOutputStream(path);
			fos.write(b);
			fos.close();

			// images
			byte[] c = video.getBytes();
			String paths = s.getServletContext().getRealPath("video/") + video.getOriginalFilename();

			System.out.println(paths);
			FileOutputStream foss = new FileOutputStream(paths);
			foss.write(c);
			foss.close();

		} catch (Exception e) {

			// TODO: handle exception
		}
		
		return ops;
	}

	//request video editing http://localhost:1001/login
	
	@GetMapping("login")
	public String login(@RequestParam("email") String email, @RequestParam("password") String pass) {

		rites ses = respo.findByLogin(email, pass);

		if (ses != null) {

			if (ses.getAdmin() == true) {
				return "redirect:/show";
			}

			return "redirect:/data";
		}

		return "index";
	}

	//request video editing http://localhost:1001/imgData
	
	@GetMapping("imgData")
	public ArrayList<img> showpages(Model m) {

		ArrayList<img> ops = (ArrayList<img>) imgRespo.findAll();

		m.addAttribute("obj", ops);
		return ops;
	}
	
	
	// this is get video REquest  from link  http://localhost:1001/videoRequestData
	@GetMapping("videoRequestData")
	public List<VideoRequest> adminAdd(Model m) {

			List<VideoRequest> vi=(List<VideoRequest>)respoVideo.findAll();
			
			m.addAttribute("obj",vi);
		
			
		return vi;
	}
	


}

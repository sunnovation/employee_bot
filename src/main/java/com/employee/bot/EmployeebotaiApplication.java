package com.employee.bot;

import com.employee.bot.conversation.ChatMessag;
import com.employee.bot.conversation.Conversation;
import com.employee.bot.conversation.repository.ConversationRepository;
import com.employee.bot.profile.Gender;
import com.employee.bot.profile.Profile;
import com.employee.bot.profile.repository.ProfileRepository;
import com.employee.bot.service.BotService;
import com.employee.bot.util.BotUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@Slf4j
public class EmployeebotaiApplication implements CommandLineRunner {

	@Autowired
	private ProfileRepository profileRepository;

	@Value("#{${employeebot.character.user}}")
	private Map<String, String> userProfileProperties;

	@Autowired
	private ConversationRepository conversationRepository;
	@Autowired
	private OpenAiChatModel chatModel;
	@Autowired
	private BotService botService;

	public static void main(String[] args) {
		SpringApplication.run(EmployeebotaiApplication.class, args);
	}



	@Override
	public void run(String... args) throws Exception {
		log.info("Running app aibot ...");
		Prompt promt=new Prompt("Who is Pawan Barthunia?");
		ChatResponse response = chatModel.call(promt);
		log.info("Message"+response.getResult().getOutput());
		profileRepository.deleteAll();
		conversationRepository.deleteAll();
		List<Profile> profiles=List.of(

				new Profile("1", "Pawan Barthunia", "Lead Software Developer", "Jeffries", 40, 23456.78,"Lead Generative AI developer", Gender.MALE, "E:\\images\\IMG_20220103_193541_007.jpg"),
				new Profile("2", "Pooja Saini", "HR Manager", "Jeffries", 32, 23456.78, "HR Represtative",Gender.FEMALE, "E:\\images\\1263.jpg"),
				new Profile("3", "Madhusmita", "Software Developer", "Default", 35, 35985.23, "SE", Gender.FEMALE,"E:\\images\\1263.jpg"),
				new Profile("4", "Meghna Sexsena", "Software Developer", "TIAA", 32, 65985.23, "Passionate about travel, photography, and new adventures. Always up for a good book and deep conversations. Let\\u0027s explore the world together!", Gender.FEMALE,"E:\\images\\1263.jpg"),
				new Profile("5", "Rashmi Desai", "Software Developer", "Jeffries", 38, 15985.23, "SE", Gender.FEMALE,"E:\\images\\1263.jpg"),
				new Profile("6", "Rekha Saini", "Software Developer", "UBS", 40, 95985.23, "SE", Gender.FEMALE,"E:\\images\\1263.jpg"),
				new Profile("7", "Rohini Sexsana", "Software Developer", "UBS", 35, 45985.23, "SE", Gender.FEMALE,"E:\\images\\1263.jpg"),
				new Profile("8", "Smitha Khandelwal", "Software Developer", "TIAA", 25, 55985.23, "SE", Gender.FEMALE,"E:\\images\\1263.jpg"),
				new Profile("9", "Mansi Malik", "Software Developer", "USB", 26, 65985.23, "SE", Gender.FEMALE,"E:\\images\\1263.jpg"),
				new Profile("10", "Alia Singh", "Software Developer", "USB", 29, 235985.23, "SE", Gender.FEMALE,"E:\\images\\1263.jpg")

		);
	   profileRepository.insert(profiles);
		Profile profileUser = new Profile(
				userProfileProperties.get("id"),
				userProfileProperties.get("name"),
				userProfileProperties.get("designation"),
				userProfileProperties.get("department"),
				Integer.parseInt(userProfileProperties.get("age")),
				Double.parseDouble(userProfileProperties.get("salary")),
				userProfileProperties.get("bio"),
				Gender.valueOf(userProfileProperties.get("gender")),
				userProfileProperties.get("imageUrl")

		);
		System.out.println(userProfileProperties);
		profileRepository.save(profileUser);

		profileRepository.findAll().forEach(
				profile ->{
					log.info(profile.toString());
				}

		);
//		botService.saveProfileToDB();

		profileRepository.findAll().stream().forEach(
				pro->{
					Conversation conversation=new Conversation(
							BotUtility.generateConversationID(),
							pro.id(),
							List.of(
									new ChatMessag("Hello ", pro.id(), LocalDateTime.now())
							)

					);
					conversationRepository.save(conversation);

				}
		);
		conversationRepository.findAll().forEach(conversation1 -> {
			log.info(conversation1.toString());
		});

	}
}

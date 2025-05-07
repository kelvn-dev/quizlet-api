package com.quizlet.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quizlet.dto.rest.request.SeedTopicDto;
import com.quizlet.dto.rest.request.WordSeedDto;
import com.quizlet.model.Topic;
import com.quizlet.model.Word;
import com.quizlet.repository.TopicRepository;
import com.quizlet.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final TopicRepository topicRepository;
    private final WordRepository wordRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("✅ DataSeeder đang chạy!");
        System.out.println("🧹 Xoá toàn bộ dữ liệu topic & word cũ...");

        wordRepository.deleteAll();     // Xóa từ vựng trước (tránh FK lỗi)
        topicRepository.deleteAll();   // Sau đó xóa topic

        System.out.println("✅ Đã xoá xong dữ liệu cũ.");


        if (topicRepository.count() == 0) {
            InputStream input = getClass().getResourceAsStream("/seed/1000_basic_words.json");
            if (input == null) {
                System.err.println("❌ Không tìm thấy file JSON! Đặt tại: src/main/resources/seed/1000_basic_words.json");
                return;
            }

            List<SeedTopicDto> topics = objectMapper.readValue(input, new TypeReference<List<SeedTopicDto>>() {});
            System.out.println("📦 Đã đọc được " + topics.size() + " chủ đề từ file JSON");

            for (SeedTopicDto seed : topics) {
                Topic topic = new Topic();
                topic.setName(seed.getName());
                topic.setPublic(true);
                topic.setUrl(seed.getUrl());
                topic.setOwnerId("google-oauth2|111479346017694677816");
                topic = topicRepository.save(topic);

                System.out.println("✔️ Đã lưu topic: " + topic.getName());

                for (WordSeedDto wordDto : seed.getWords()) {
                    Word word = new Word();
                    word.setName(wordDto.getName());
                    word.setDefinition(wordDto.getDefinition());
                    word.setTopicId(topic.getId());
                    wordRepository.save(word);

                    System.out.println("  ↳ từ: " + word.getName());
                }
            }
            System.out.println("🎉 Seed dữ liệu hoàn tất.");
        } else {
            System.out.println("ℹ️ Topic đã tồn tại. Bỏ qua seed.");
        }
    }
}

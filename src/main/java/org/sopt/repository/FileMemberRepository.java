package org.sopt.repository;

import org.sopt.domain.Gender;
import org.sopt.domain.Member;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

import static org.sopt.constant.ErrorMessage.*;

public class FileMemberRepository implements MemberRepository {
    private static final String FILE_PATH = "members.txt";
    private final Map<Long, Member> idMap = new HashMap<>();
    private final Map<String, Long> emailMap = new HashMap<>();

    public FileMemberRepository() {
        initFile();
        loadAllToMemory();
    }

    private void initFile() {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(FILE_INIT_FAILED.getMessage());
        }
    }

    public Member save(Member member) {
        idMap.put(member.getId(), member);
        emailMap.put(member.getEmail(), member.getId());
        syncToFile();
        return member;
    }

    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(idMap.get(id));
    }

    public List<Member> findAll() {
        return new ArrayList<>(idMap.values());
    }

    public boolean existsByEmail(String email) {
        return emailMap.containsKey(email.toLowerCase());
    }

    public void deleteById(Long id) {
        Member removed = idMap.remove(id);
        if (removed != null) {
            emailMap.remove(removed.getEmail().toLowerCase());
            syncToFile();
        }
    }

    private void loadAllToMemory() {
        try (var br = Files.newBufferedReader(Paths.get(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                Member member = parseMember(line);
                idMap.put(member.getId(), member);
                emailMap.put(member.getEmail().toLowerCase(), member.getId());
            }
        } catch (IOException e) {
            throw new RuntimeException(MEMBER_SAVE_FAILED.getMessage());
        }
    }

    private void syncToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, false))) {
            for (Member member : idMap.values()) {
                writer.write(formatMember(member));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(FILE_UPDATE_FAILED.getMessage());
        }
    }

    private String formatMember(Member member) {
        return member.getId() + "," +
               member.getName() + "," +
               member.getBirthDate() + "," +
               member.getEmail() + "," +
               member.getGender();
    }

    private Member parseMember(String line) {
        String[] parts = line.split(",");
        Long id = Long.parseLong(parts[0]);
        String name = parts[1];
        LocalDate birthDate = LocalDate.parse(parts[2]);
        String email = parts[3];
        Gender gender = Gender.valueOf(parts[4].toUpperCase());
        return new Member(id, name, birthDate, email, gender);
    }

}

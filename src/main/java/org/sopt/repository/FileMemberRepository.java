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
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.sopt.constant.ErrorMessage.*;

public class FileMemberRepository implements MemberRepository {
    private static final String FILE_PATH = "members.txt";

    public FileMemberRepository() {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            throw new RuntimeException(FILE_INIT_FAILED.getMessage());
        }
    }

    public Member save(Member member) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(formatMember(member));
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException(MEMBER_SAVE_FAILED.getMessage());
        }
        return member;
    }

    public Optional<Member> findById(Long id) {
        return findAll().stream()
                .filter(m -> Objects.equals(m.getId(), id))
                .findFirst();
    }

    public List<Member> findAll() {
        try (var lines = Files.lines(Paths.get(FILE_PATH))) {
            return lines.filter(line -> !line.isBlank())
                    .map(this::parseMember)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(MEMBER_LIST_FAILED.getMessage());
        }
    }

    public boolean existsByEmail(String email) {
        return findAll().stream()
                .anyMatch(m -> m.getEmail().equalsIgnoreCase(email));
    }

    public void deleteById(Long id) {
        List<Member> members = findAll();
        List<Member> updated = members.stream()
                .filter(m -> !Objects.equals(m.getId(), id))
                .collect(Collectors.toList());
        overwriteFile(updated);
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

    private void overwriteFile(List<Member> members) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, false))) {
            for (Member member : members) {
                writer.write(formatMember(member));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(FILE_UPDATE_FAILED.getMessage());
        }
    }
}

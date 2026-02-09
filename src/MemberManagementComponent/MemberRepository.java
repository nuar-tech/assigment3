package MemberManagementComponent;

import edu.aitu.oop3.db.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class MemberRepository {
    public Member findById(int id) throws SQLException, MemberNotFoundException {
        String sql = "SELECT * FROM members WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapToMember(rs);
            } else {
                throw new MemberNotFoundException(id);
            }
        }
    }

    public Member findByMemberId(String memberId) throws SQLException, MemberNotFoundException {
        String sql = "SELECT * FROM members WHERE member_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, memberId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapToMember(rs);
            } else {
                throw new MemberNotFoundException("member_id", memberId);
            }
        }
    }

    public List<Member> findAll() throws SQLException {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM members";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                members.add(mapToMember(rs));
            }
        }
        return members;
    }

    private Member mapToMember(ResultSet rs) throws SQLException {
        Member member = new Member();
        member.setId(rs.getInt("id"));
        member.setMemberId(rs.getString("member_id"));
        member.setName(rs.getString("name"));
        member.setEmail(rs.getString("email"));
        member.setPhone(rs.getString("phone"));
        member.setJoinDate(rs.getDate("join_date").toLocalDate());
        return member;
    }
}
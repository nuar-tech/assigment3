package MemberManagementComponent;

import exception.LibraryException;

public class MemberNotFoundException extends LibraryException {
    public MemberNotFoundException(String message) {
        super(message);
    }

    public MemberNotFoundException(int memberId) {
        super("Member with ID " + memberId + " not found");
    }

    public MemberNotFoundException(String field, String value) {
        super("Member with " + field + " = '" + value + "' not found");
    }
}
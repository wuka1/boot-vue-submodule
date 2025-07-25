@id("Group #1")
permit (
    principal in Role::"viewSalary",
    action == Action::"viewSalary",
    resource == Resource::"/list/salary"
);
permit (
    principal,
    action == Action::"viewSalary",
    resource
)
when {
    ip(context.ip).isInRange(ip("192.168.0.1/24"))
};
@id("Policy #1")
permit (
    principal,
    action == Action::"viewSalary",
    resource
)
when {
    principal == resource.owner ||
    principal == resource.owner.manager
};
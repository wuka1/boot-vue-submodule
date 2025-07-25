permit (
    principal,
    action == PayrollApp::Action::"viewSalary",
    resource
)
when {
    principal == resource.owner.manager ||
    principal == resource.owner ||

    principal == resource.owner.manager ||
    principal == resource.owner ||
    principal == resource.attrs.owner ||
    principal == context.owner
};
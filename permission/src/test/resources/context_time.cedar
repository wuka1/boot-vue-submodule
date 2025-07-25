@id("Context_time #1")
permit (
    principal,
    action == Action::"viewSalary",
    resource
)
when {
    context.now - resource.timePolicy.startTime > 0 &&
    resource.timePolicy.endTime - context.now > 0
};
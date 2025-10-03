// Scenario 1A: One Principal, One Action, One Resource
permit (
  principal == User::"alice",
  action == Action::"view",
  resource == Photo::"VacationPhoto94.jpg"
)when {
   ip(context.source_ip).isInRange(ip("192.168.0.1/24"))
};

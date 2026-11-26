const seedTasks = [
  { id: "1", title: "Complete project proposal", description: "Draft and send the proposal to the client.", dueDate: "2026-10-15", priority: "high", assigneeId: "aarav", completed: false },
  { id: "2", title: "Review design mockups", description: "Provide feedback on new UI designs.", dueDate: "2026-10-12", priority: "medium", assigneeId: "aarav", completed: true },
  { id: "3", title: "Team standup meeting", description: "Daily sync with the development team.", dueDate: "2026-10-10", priority: "low", assigneeId: "aarav", completed: false },
  { id: "4", title: "Update portfolio website", description: "Add recent projects and update the about section.", dueDate: null, priority: "medium", assigneeId: "aarav", completed: false },
  { id: "5", title: "Prepare client presentation", description: "Create slides for the quarterly business review.", dueDate: "2026-10-18", priority: "high", assigneeId: "priya", completed: false },
  { id: "6", title: "Fix login bug", description: "Resolve Google OAuth login issue.", dueDate: "2026-10-11", priority: "high", assigneeId: "priya", completed: true },
  { id: "7", title: "Write blog post", description: "Draft an article on mobile apps for business.", dueDate: "2026-10-25", priority: "low", assigneeId: "priya", completed: false },
  { id: "8", title: "Test payment gateway", description: "Run end-to-end Stripe tests on staging.", dueDate: "2026-10-14", priority: "medium", assigneeId: "priya", completed: false },
  { id: "9", title: "Update documentation", description: "Add API endpoint details to README.", dueDate: "2026-10-22", priority: "low", assigneeId: "priya", completed: false },
  { id: "10", title: "Design new landing page", description: "High-fidelity mockup for product launch.", dueDate: "2026-10-16", priority: "high", assigneeId: "rohan", completed: false },
  { id: "11", title: "Conduct user interviews", description: "Schedule 5 user interviews for beta feedback.", dueDate: "2026-10-19", priority: "medium", assigneeId: "rohan", completed: false },
  { id: "12", title: "Optimise database queries", description: "Improve query performance for task list.", dueDate: "2026-10-13", priority: "high", assigneeId: "rohan", completed: true },
  { id: "13", title: "Set up CI/CD pipeline", description: "Configure GitHub Actions for testing and deployment.", dueDate: "2026-10-21", priority: "medium", assigneeId: "rohan", completed: false },
  { id: "14", title: "Review pull requests", description: "Review and merge pending PRs.", dueDate: "2026-10-10", priority: "low", assigneeId: "rohan", completed: true },
  { id: "15", title: "Plan sprint retrospective", description: "Organise the sprint retrospective meeting.", dueDate: "2026-10-23", priority: "low", assigneeId: "rohan", completed: false }
];

module.exports = seedTasks;

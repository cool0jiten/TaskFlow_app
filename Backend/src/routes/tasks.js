const express = require("express");
const { readTasks, writeTasks } = require("../store");

const router = express.Router();
const ALLOWED_PRIORITIES = ["low", "medium", "high"];
const ALLOWED_ASSIGNEES = ["aarav", "priya", "rohan"];
const ALLOWED_STATUSES = ["active", "completed"];

// GET /api/tasks?assignee=aarav&status=active
router.get("/", (req, res) => {
  const { assignee, status } = req.query;
  const normalizedAssignee = assignee ? String(assignee).toLowerCase() : null;
  const normalizedStatus = status ? String(status).toLowerCase() : null;

  if (normalizedAssignee && !ALLOWED_ASSIGNEES.includes(normalizedAssignee)) {
    return res.status(400).json({ message: "Invalid assignee. Allowed: aarav, priya, rohan." });
  }
  if (normalizedStatus && !ALLOWED_STATUSES.includes(normalizedStatus)) {
    return res.status(400).json({ message: "Invalid status. Allowed: active, completed." });
  }

  let tasks = readTasks();
  if (normalizedAssignee) tasks = tasks.filter(t => t.assigneeId === normalizedAssignee);
  if (normalizedStatus === "active") tasks = tasks.filter(t => !t.completed);
  if (normalizedStatus === "completed") tasks = tasks.filter(t => t.completed);
  return res.json(tasks);
});

// POST /api/tasks
router.post("/", (req, res) => {
  const body = req.body || {};
  const { title, description = "", dueDate = null, priority, assigneeId, completed = false } = body;
  const errors = {};

  if (typeof title !== "string" || !title.trim()) errors.title = "Title is required and cannot be empty.";
  if (!ALLOWED_PRIORITIES.includes(priority)) errors.priority = "Priority must be low, medium, or high.";
  if (!ALLOWED_ASSIGNEES.includes(assigneeId)) errors.assigneeId = "Assignee must be aarav, priya, or rohan.";
  if (dueDate !== null && (typeof dueDate !== "string" || !/^\d{4}-\d{2}-\d{2}$/.test(dueDate) || Number.isNaN(Date.parse(`${dueDate}T00:00:00Z`)))) {
    errors.dueDate = "Due date must be a valid YYYY-MM-DD date or null.";
  }
  if (typeof completed !== "boolean") errors.completed = "Completed must be a boolean.";
  if (typeof description !== "string") errors.description = "Description must be a string.";

  if (Object.keys(errors).length) return res.status(400).json({ message: "Validation failed", errors });

  const tasks = readTasks();
  const newTask = {
    id: String(Date.now()),
    title: title.trim(),
    description: description.trim(),
    dueDate,
    priority,
    assigneeId,
    completed
  };
  tasks.unshift(newTask);
  writeTasks(tasks);
  return res.status(201).json(newTask);
});

// PATCH /api/tasks/:id/toggle
router.patch("/:id/toggle", (req, res) => {
  const tasks = readTasks();
  const index = tasks.findIndex(task => task.id === String(req.params.id));
  if (index === -1) return res.status(404).json({ message: `Task with id ${req.params.id} was not found.` });

  tasks[index].completed = !tasks[index].completed;
  writeTasks(tasks);
  return res.json(tasks[index]);
});

module.exports = router;

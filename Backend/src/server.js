const express = require("express");
const cors = require("cors");
const tasksRouter = require("./routes/tasks");
const { readTasks } = require("./store");

const app = express();
const PORT = Number(process.env.PORT) || 3000;

app.use(cors());
app.use(express.json());

app.get("/", (_req, res) => {
  res.json({ name: "TaskFlow API", status: "running", endpoints: ["GET /api/tasks", "POST /api/tasks", "PATCH /api/tasks/:id/toggle"] });
});
app.get("/health", (_req, res) => res.json({ status: "ok" }));
app.use("/api/tasks", tasksRouter);
app.use((_req, res) => res.status(404).json({ message: "Route not found" }));
app.use((err, _req, res, _next) => {
  console.error(err);
  res.status(500).json({ message: "Internal server error" });
});

readTasks();
app.listen(PORT, "0.0.0.0", () => console.log(`TaskFlow API listening on http://localhost:${PORT}`));

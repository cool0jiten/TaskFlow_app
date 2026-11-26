const fs = require("fs");
const path = require("path");
const seedTasks = require("./data/seedTasks");

const DATA_FILE = path.join(__dirname, "data", "tasks.json");

function ensureDataFile() {
  if (!fs.existsSync(DATA_FILE)) {
    fs.writeFileSync(DATA_FILE, JSON.stringify(seedTasks, null, 2), "utf8");
  }
}

function readTasks() {
  ensureDataFile();
  return JSON.parse(fs.readFileSync(DATA_FILE, "utf8"));
}

function writeTasks(tasks) {
  fs.writeFileSync(DATA_FILE, JSON.stringify(tasks, null, 2), "utf8");
}

module.exports = { readTasks, writeTasks };

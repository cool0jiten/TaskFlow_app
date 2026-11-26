# TaskFlow Backend (Node.js + Express)

A minimal REST API for the TaskFlow Android assessment. It uses Express and stores task data in a JSON file, so no database setup is required. The seed dataset is initialized automatically on first start.

## Requirements
- Node.js 18+
- npm

## Run locally
```bash
npm install
npm start
```

The server runs on `http://localhost:3000` by default. Set a different port with `PORT=4000 npm start`.

## Endpoints

### GET `/api/tasks`
Returns all tasks. Optional filters can be combined:
- `?assignee=aarav` (allowed: `aarav`, `priya`, `rohan`)
- `?status=active` or `?status=completed`

Examples:
```bash
curl http://localhost:3000/api/tasks
curl 'http://localhost:3000/api/tasks?assignee=aarav&status=active'
```

### POST `/api/tasks`
Creates a task. `title` must be non-empty; priority must be `low`, `medium`, or `high`; assignee must be `aarav`, `priya`, or `rohan`; dueDate is `YYYY-MM-DD` or `null`.

Example:
```bash
curl -X POST http://localhost:3000/api/tasks \
  -H 'Content-Type: application/json' \
  -d '{"title":"Prepare release","description":"QA and release","dueDate":"2026-12-01","priority":"high","assigneeId":"aarav","completed":false}'
```

Validation errors return HTTP 400:
```json
{
  "message": "Validation failed",
  "errors": {
    "title": "Title is required and cannot be empty."
  }
}
```

### PATCH `/api/tasks/:id/toggle`
Toggles completion and returns the updated task.
```bash
curl -X PATCH http://localhost:3000/api/tasks/1/toggle
```
Returns HTTP 404 if the task ID does not exist.

### Health check
`GET /health`

## Seed data and persistence
The 15 assessment tasks are stored in `src/data/seedTasks.js`. On first launch, the server creates `src/data/tasks.json` from that seed. Subsequent changes (creating or toggling tasks) are saved in `tasks.json` and persist across restarts. Delete `src/data/tasks.json` to reset to the original seed dataset.

## Android emulator connection
For the Android emulator, use `http://10.0.2.2:3000/` as the API base URL. For a physical Android device, use the computer's LAN IP and ensure both devices are on the same network.

For local HTTP development, Android may require cleartext HTTP to be enabled in the app manifest/network security configuration.

import APIWrapper from '../api/accntbltyAPIWrapper.js';

class FailedTasks {
  constructor() {}

  async getTasksByUserId(userId) {
    return APIWrapper.getTasksByUserId(userId);
  }

  async listBuilder(tasks) {
    const table = document.getElementById('table-body');
    tasks.forEach((task) => {
      if (task.status == "Failed") {
          let row = table.insertRow();
          row.id = task.taskId;
          let taskName = row.insertCell(0);
          taskName.innerHTML = task.taskName;
          let taskDescription = row.insertCell(1);
          taskDescription.innerHTML = task.taskDescription;
          let failureReason = row.insertCell(2);
          failureReason.innerHTML = "<span class = 'red-text'>" + task.failureReason + "</span>";
      }
    });
  }
}

const main = async () => {
  const failedTasks = new FailedTasks();
  const username = localStorage.getItem('username');
  document.getElementById('welcome-message').innerHTML = `Time to review your mistakes, ${username}!`;
  const tasks = await failedTasks.getTasksByUserId(username);
  await failedTasks.listBuilder(tasks);
};

window.addEventListener('DOMContentLoaded', main);

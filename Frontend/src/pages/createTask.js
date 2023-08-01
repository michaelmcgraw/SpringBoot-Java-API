import APIWrapper from '../api/accntbltyAPIWrapper.js';

class CreateTask {

    constructor() {
        this.userId = localStorage.getItem("username");
    }

    createTask() {
        const taskName = document.getElementById("task-name");
        const taskDescription = document.getElementById("task-description");
        console.log(this.userId, taskName.value, taskDescription.value);
        APIWrapper.createTask(this.userId, taskName.value, taskDescription.value);
        const task = {name: taskName.value, description: taskDescription.value}; // Store the task details
        taskName.value = ''; // Clear the task name field
        taskDescription.value = ''; // Clear the task description field
        return task; // Return the task details
    }

    returnToHome() {
        console.log("returning home");
        location.href = "/home.html";
    }

}

const main = async () => {
    const createTask = new CreateTask();
    document.getElementById("returnToHomeButton").addEventListener("click", () => createTask.returnToHome());
    document.getElementById("submitButton").addEventListener("click", () => {
        const task = createTask.createTask();
        document.getElementById("taskName").innerText = task.name;
        document.getElementById("taskDescription").innerText = task.description;
        modal.style.display = "block";
    });
};

window.addEventListener("DOMContentLoaded", main);

var modal = document.getElementById("myModal");
var span = document.getElementsByClassName("close")[0];

span.onclick = function() {
  modal.style.display = "none";
}

window.onclick = function(event) {
  if (event.target == modal) {
    modal.style.display = "none";
  }
}

import APIWrapper from '../api/accntbltyAPIWrapper.js';

const insults = [
          "Did your brain take a vacation, or is it just permanently out of office?",
          "You're the reason why they invented the word 'slacker.'",
          "Congratulations on your outstanding achievement in procrastination!",
          "If they gave out awards for not getting things done, you'd be a legend.",
          "I didn't realize 'unfinished' was your signature style.",
          "You must have been trained by a snail in the art of productivity.",
          "You make procrastination look like an Olympic sport.",
          "I bet even gravity gets frustrated with how slowly you move.",
          "Is there a black hole where your motivation should be?",
          "You're like a broken pencil, always pointless and never getting to the point.",
          "You're as useful as a screen door on a submarine.",
          "Even a sloth on sedatives would move faster than you.",
          "Your productivity level is on par with a hibernating bear.",
          "If procrastination were an art form, you'd be Picasso.",
          "You have a remarkable talent for avoiding anything resembling work.",
          "Are you allergic to getting things done, or is it just a personal choice?",
          "I'm beginning to think your spirit animal is a sloth.",
          "You're the master of starting things and never finishing them.",
          "You're a shining example of what not to do in the realm of productivity.",
          "Time waits for no one, but apparently, it waits for you."
      ]


class Home {
  constructor() {}

  async getTasksByUserId(userId) {
    return APIWrapper.getTasksByUserId(userId);
  }

  async failTask(task) {
    // Get the modal
    var modal = document.getElementById("failModal");

    // Get the <span> element that closes the modal
    var span = document.getElementsByClassName("close")[0];

    // When the user clicks on <span> (x), close the modal
    span.onclick = function() {
      modal.style.display = "none";
    }

    // When the user clicks anywhere outside of the modal, close it
    window.onclick = function(event) {
      if (event.target == modal) {
        modal.style.display = "none";

      }
    }

    // Show the modal
    modal.style.display = "block";

    // Generate a random insult
    const insultIndex = Math.floor(Math.random() * insults.length);
    const insult = insults[insultIndex];

    // Add the insult to the modal's text content
    document.querySelector("#failModal p").textContent = insult;

    // When the user clicks on the button, close the modal and fail the task
    document.getElementById("submitFailure").onclick = async function() {
      var failureReason = document.getElementById("failureReason").value;
      await APIWrapper.failTask(task.taskId, failureReason);
      console.log("Failed Task " + task.taskName + " because " + failureReason);
      document.getElementById(task.taskId).remove();
      modal.style.display = "none";
      document.getElementById("failureReason").value = ""; // Clear the text box
    }
  }

  async succeedTask(task) {
    // Get the modal
    var modal = document.getElementById("succeedModal");

    // Get the <span> element that closes the modal
    var span = document.getElementsByClassName("close")[0];

    // When the user clicks on <span> (x), close the modal
    span.onclick = function() {
      modal.style.display = "none";
    }

    // When the user clicks anywhere outside of the modal, close it
    window.onclick = function(event) {
      if (event.target == modal) {
        modal.style.display = "none";
      }
    }

    // Show the modal
    modal.style.display = "block";

    // When the user clicks on the button, close the modal and succeed the task
    document.getElementById("submitSuccess").onclick = async function() {
      await APIWrapper.succeedTask(task.taskId);
      console.log("Succeeded Task " + task.taskName);
      modal.style.display = "none";
    }
  }

  async listBuilder(tasks) {
    const table = document.getElementById('table-body');
    tasks.forEach((task) => {
      if (task.status == "In Progress") {
          let row = table.insertRow();
          row.id = task.taskId;
          let taskName = row.insertCell(0);
          taskName.innerHTML = task.taskName;
          let taskDescription = row.insertCell(1);
          taskDescription.innerHTML = task.taskDescription;

          // Add action buttons
          const removeRow = () => {
            document.getElementById(task.taskId).remove();
          }

          let succeedTaskCell = row.insertCell(2);
          var succeedButton = document.createElement("BUTTON");
          succeedButton.className = "succeed-button";
          succeedButton.addEventListener("click", () => this.succeedTask(task));
          succeedButton.addEventListener("click", removeRow);
          succeedButton.innerHTML = 'Succeed';
          succeedTaskCell.appendChild(succeedButton);

          let failTaskCell = row.insertCell(3);
          var failButton = document.createElement("BUTTON");
          failButton.className = "fail-button";
          failButton.addEventListener("click", () => this.failTask(task));
          failButton.innerHTML = 'Fail';
          failTaskCell.appendChild(failButton);
      }
    });
  }
}

const main = async () => {
  const home = new Home();
  const username = localStorage.getItem('username');
  document.getElementById('welcome-message').innerHTML = `Welcome, ${username}!`;
  const tasks = await home.getTasksByUserId(username);
  await home.listBuilder(tasks);
};

window.addEventListener('DOMContentLoaded', main);

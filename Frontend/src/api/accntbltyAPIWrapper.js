import axios from 'axios';


class APIWrapper {
  getTasksByUserId = async (userId) => {
      const response = await axios.get('/tasks?userId=' + userId);
      console.log(response.data);
      return response.data;
  }

  getTask = async (taskId) => {
      const response = await axios.get('/tasks/' + taskId);
      console.log(response);
      return response.data;
  }

  createTask = async (userId, taskName, taskDescription) => {
      const response = await axios.post('/tasks', {
          "userId" : userId,
          "taskName" : taskName,
          "taskDescription" : taskDescription
      })
      console.log(response.data.taskId);
      return response.data.taskId;
  }

  updateTask = async (taskId, status, failureReason) => {
      const response = await axios.put('/tasks', {
          "taskId" : taskId,
          "status" : status,
          "failureReason" : failureReason
      })
      console.log(response.status == 200);
      return (response.status == 200);
  }

  succeedTask = async (taskId) => {
      console.log("Entered succeedTask");
      const response = this.updateTask(taskId, "Succeeded", "");
      return response;
  }

  failTask = async (taskId, failureReason) => {
      console.log("Entered succeedTask");
      const response = this.updateTask(taskId, "Failed", failureReason);
      return response;
  }


}
export default new APIWrapper();
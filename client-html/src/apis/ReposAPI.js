import ApiWrapper from "/src/apis/APIWrapper.js";

export class ReposAPI extends ApiWrapper {
  constructor() {
    super();
 
  }

  async getRepos() {
    return this.getRequest(`/api/repos/`);
  }

  

}

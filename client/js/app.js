//base url http://localhost:8080/api/agent
//Fetch Request
function displayAgents(){
    getAgents().then(data => renderList(data));
}

function getAgents(){
    return fetch('http://localhost:8080/api/agent')
    .then(response =>{
        return response.json();
    })
}
function getAgentById(agentId){
    return fetch('http://localhost:8080/api/agent/'+agentId)
    .then(response => {
        return response.json();
    })
}
function renderList(agents){
    console.log(agents);

    const tableBodyElement = document.getElementById("tableRows");
    const agentHTML = agents.map(agent => {
        let dob = agent.dob;
        if(dob == null || dob == ""){
            dob="REDACTED";
        }
        return `
        <tr>
            <td id="nameRow"  onclick="getAgentCard(${agent.agentId})">${agent.firstName} ${agent.middleName} ${agent.lastName} </td>
            <td>${dob}</td>
            <td>${agent.heightInInches}</td>
            <td>
                <button onclick="handleUpdate(${agent.agentId})" class="btn btn-primary">update</button>
                <button onclick="handleDelete(${agent.agentId})" class="btn btn-danger">delete</button>
            </td>
        </tr>
        `;
    });
    tableBodyElement.innerHTML = agentHTML.join('');
}

function renderErrors(errors){
    const errorsHtml = errors.map(error => {
        let html =`<li>${error}</li>`
        return html;
    });
    const errorsHtmlString = `
        <p> The following errors were found:</p>
        <ul>
            ${errorsHtml.join('')}
        </ul>
    `;
    document.getElementById('errors').innerHTML = errorsHtmlString;
    document.getElementById("errors").style.display = "block";

    
}
function getAgentCard(agentId){
    getAgentById(agentId)
    .then(agent =>{
        const fullName = agent.firstName + " " + agent.middleName + " " + agent.lastName;
        const height = agent.heightInInches;
        let DOB = agent.dob;
        
        if(DOB == null){
            DOB ="REDACTED";
        }
        const message = "Name: "+ fullName + "\nDOB: " + DOB +"\nHeight in Inches: " + height + "\nAliases: " + agent.aliases + "\nAgencies: " + agent.agencies;
        alert(message);
    }
    );
    
}
function handleUpdate(agentId){
    alert("Agent ID: " + agentId);    
}
function handleDelete(agentId){
    alert("Agent ID: " + agentId);
}
function handleSubmit(event){
    event.preventDefault();
    document.getElementById("errors").style.display = "none";

    const agent ={
        firstName: document.getElementById('firstName').value,
        middleName: document.getElementById('middleName').value,
        lastName: document.getElementById('lastName').value,
        dob: document.getElementById('dob').value,
        heightInInches: document.getElementById('heightInInches').value ? parseInt(document.getElementById('heightInInches').value) : 0,
    };  
    
    const init ={
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body : JSON.stringify(agent)
    };

    fetch('http://localhost:8080/api/agent',init).then(response =>{
        if(response.status === 201 || response.status === 400){
            return response.json();
        }
        else{
            return Promise.reject(`Unexpected Status Code: ${response.status}`);
        }
    })
    .then(data => {
        if(data.agentId){
            alert("AGENT ADDED");
            displayAgents();
            event.target.reset()/// event target is form
        }else{
            //TODO RENDER ERROR MESSAGE
            
            renderErrors(data);
        }
    })
    .catch(error => alert(error));
}


displayAgents();


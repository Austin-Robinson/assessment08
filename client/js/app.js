//base url http://localhost:8080/api/agent
//Fetch Request
let agents = []
let editAgentId =-1;
function displayAgents(){
    getAgents().then(data => {
        agents = data;
        renderList(data)
    });
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
    const agent = agents.find(agent => agent.agentId === agentId);
    console.log(agent);

        document.getElementById('firstName').value = agent.firstName;
        document.getElementById('middleName').value = agent.middleName;
        document.getElementById('lastName').value = agent.lastName;
        document.getElementById('dob').value = agent.dob;
        document.getElementById('heightInInches').value = agent.heightInInches;

       document.getElementById('formSubmit').innerText = 'Update Agent';

       editAgentId = agentId;

//http://localhost:8080/api/agent



}
function handleDelete(agentId){

    const agent = agents.find(agent => agent.agentId === agentId);

    if(confirm(`Delete Agent ${agent.firstName} ${agent.lastName}.`)){
        const init = {
            method: 'DELETE'
        };
        fetch(`http://localhost:8080/api/agent/${agentId}`,init)
        .then(response =>{
            if(response.status === 204){
                displayAgents();
                resetState();
            }else{
                return Promise.reject(`Unexpected Status Code: ${response.status}`);
            }
        }).catch(console.log);
    }
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
    if(editAgentId != -1){
        doPut(agent, event)
    }else{
    doPost(agent,event);
    }
}
function goToBottom(elementId){
    const objDiv = document.getElementById(elementId);
    objDiv.scrollTop = objDiv.scrollHeight;
}

function doPost(agent,event){
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
            goToBottom("errors")
        }
    })
    .catch(error => alert(error));
}
function resetState(){
    document.getElementById('formSubmit').innerText = 'Add Agent';
    document.getElementById('errors').innerHTML ='';
    document.getElementById('form').reset();
    editAgentId = -1;

}

function doPut(agent, event){
    agent.agentId = editAgentId;
    
    const init = {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(agent)
    };

    fetch(`http://localhost:8080/api/agent/${editAgentId}`,init)
    .then(response => { //404 
        if(response.status === 204){
            return null;
        }
        else if(response.status === 400){
            return response.json();
        }else{
            return Promise.reject(`Unexpected Status Code: ${response.status}`);
        }
    })
    .then(data => {
        if(!data){
            displayAgents();
            event.target.reset()/// event target is form
            resetState();
        }
        else{
            console.log(data);
            renderErrors(data);
        }
    })
    .catch(console.log);
}

displayAgents();


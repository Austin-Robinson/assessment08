//base url http://localhost:8080/api/agent
//Fetch Request
function init(){
    getAgents().then(data => renderList(data));
}

function getAgents(){
    return fetch('http://localhost:8080/api/agent')
    .then(response =>{
        return response.json();
    })
}
function renderList(agents){
    console.log(agents);

    const tableBodyElement = document.getElementById("tableRows");
    const agentHTML = agents.map(agent => {
    return `
    <tr>
        <td>${agent.firstName} ${agent.middleName} ${agent.lastName}</td>
        <td>${agent.dob}</td>
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
function handleUpdate(agentId){
    alert("Agent ID: " + agentId);    
}
function handleDelete(agentId){
    alert("Agent ID: " + agentId);
}

init();

/*fetch('http://localhost:8080/api/agent')
    .then(response =>{
        return response.json();
    })
    .then(data => {
        console.log(data);

        const tableBodyElement = document.getElementById("tableRows");
        const agentHTML = data.map(agent => {
            return `
            <tr>
                <td>${agent.firstName}-${agent.middleName}-${agent.lastName}Name</td>
                <td>${agent.dob}</td>
                <td>${agent.heightInInches}</td>
                <td>
                    <button class="btn btn-primary">update</button>
                    <button class="btn btn-danger">delete</button>
                </td>
            </tr>
        `;
        });
        tableBodyElement.innerHTML = agentHTML.join('');
    });
*/

    /*
      private int agentId;
    private String firstName;
    private String middleName;
    private String lastName;
    private LocalDate dob;
    private int heightInInches;

    private List<Alias> aliases = new ArrayList<>();

    public List<Alias> getAliases() {
        return aliases;
    }
    */
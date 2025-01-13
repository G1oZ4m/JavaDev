const display = document.getElementById("display");

function appendToDisplay(input){
    display.value += input;

}

function clearDisplay(){
    display.value = "";
}

function calculate() {
    const expression = document.getElementById("display").value; // Receiving expression from text field

    fetch("http://localhost:8001/calculate", {
        method: "POST",
        headers: {"Content-Type": "text/plain"},
        body: expression, // Sending expression
    })
    .then(response => response.text())
    .then(result => {
        document.getElementById("display").value = result; // Showing result on website
    })
    .catch(error => console.error("Error: ", error));
}

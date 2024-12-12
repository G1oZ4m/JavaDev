const display = document.getElementById("display");

function appendToDisplay(input){
    display.value += input;

}

function clearDisplay(){
    display.value = "";
}

function calculate(){
    display.value = eval(display.value);
}

function calculate() {
    const expression = document.getElementById("display").value;

    fetch("http://localhost:8000/calculate", {
        method: "POST",
        headers: { "Content-Type": "text/plain" },
        body: expression,
    })
    .then(response => response.text())
    .then(result => {
        document.getElementById("display").value = result;
    })
    .catch(error => console.error("Error:", error));
}


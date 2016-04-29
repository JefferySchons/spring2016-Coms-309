print("JavaScript working.");

function dumpVariables() {
	out.println("");
	out.println("Dumping JS Variables");
	for (var variable in this) {
		out.println(variable);
	}
	out.println("");
}

while (true) {
	Server.runServer();
	out.println("Restarting server...");
}
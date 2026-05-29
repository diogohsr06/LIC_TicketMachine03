# Extract the ideas, everyone makes mistakes

✅ Done  
❗ Needs testing  
🟨 Incomplete/on going  
❌ Yet to implement  

**Hardware**:  
Key Decode ✅  
Ring Buffer ✅ ❗   
Key Transmitter ✅ ❗ 
❗Issue: Stops transmitting after first key, reliant on RESET for more keys. This has been fixed on ModelSim Recently for good, still needs real hardware testing. Cause: RBC was stuck on reading state, incGet is never activated, thus, read pointer never advances, causing it to be stuck on the first key. RESET fixed this by clearing the registers, forcing the machine to initial state.  
Port Expander LCD ✅  
Port Expander TD ✅  
Coin Acceptor ✅  
M ✅  

**Software**:  
HAL ✅  
KBD ✅  
KeyReceiver ✅  
Serial Emitter ✅  
LCD ✅  
Ticket Dispenser ✅  
Coin acceptor ✅  
M ✅  
File Access ✅  
Stations ✅  
Coin Deposit ✅  
Others/Utils ✅  
TUI ✅  
TicketMachine ✅  



<img width="950" height="580" alt="TicketMachineDiagram" src="https://github.com/user-attachments/assets/356d093d-4738-43c9-a6dd-0710f630b918" />

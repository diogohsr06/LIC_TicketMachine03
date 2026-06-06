---------------------------------------------------------------------------------------------
-- Serial Receiver
---------------------------------------------------------------------------------------------

library ieee;
use IEEE.std_logic_1164.all;

entity SerialReceiver is
    port
        (
			--Inputs
			SDX: in std_logic;							--Serial Data in
			SCLK: in std_logic;							--Relogio
			SS: in std_logic;								--Enable
			RESET: in std_logic;							--Reinicia o sistema
			
			--Outputs
			Q: out std_logic_vector(9 downto 0));	--Data out
end SerialReceiver;

architecture arch_SER of SerialReceiver is
--Registo de deslocamento 10 bits
component ShiftRegisterPELCD is
    port
        (
			SerialIN: in std_logic;
			SCLK: in std_logic;
			SS: in std_logic;
			RESET: in std_logic;
			Q: out std_logic_vector(9 downto 0));
end component;
signal Q_out: std_logic_vector(9 downto 0);
--Registo 10 bits
component HoldRegisterPELCD is
    port
        (
			D: in std_logic_vector(9 downto 0);
			CLK: in std_logic;
			RESET: in std_logic;
			Q: out std_logic_vector(9 downto 0));
end component;

--Sinais intermedios
signal notSS: std_logic;

begin

notSS <= not SS;							--Enable active low

SR: ShiftRegisterPELCD port map(		--Instanciaçao do Shift Register
	 SerialIN => SDX,
	 SCLK => SCLK,
	 SS => notSS,
	 RESET => RESET,
	 Q => Q_out);
	 
HR: HoldRegisterPELCD port map(		--Instanciaçao do Hold Register
	 D => Q_out,
	 CLK => SS,
	 RESET => RESET,
	 Q => Q);
	 
end arch_SER;
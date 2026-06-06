---------------------------------------------------------------------------------------------
-- Counter Register 3 bits
---------------------------------------------------------------------------------------------

LIBRARY ieee;
USE ieee.std_logic_1164.ALL;

ENTITY CounterRegister3 IS
	PORT(	
		  --Inputs
		  RESET: IN STD_LOGIC;							--Clear da maquina
		  CE, CLK: IN STD_LOGIC;						--Count enable e relogio da FPGA
		  TC: OUT STD_LOGIC;								--Terminal Count (limite da contagem)
		  
		  --Outputs
		  Q: OUT STD_LOGIC_VECTOR(2 downto 0)		--Valor de contagem
);
END CounterRegister3;

ARCHITECTURE arch_CR OF CounterRegister3 IS
--Registo 1 bit
COMPONENT FlipFlop
		PORT(
			  CLK : IN STD_LOGIC;
			  RESET : IN STD_LOGIC;
			  D : IN STD_LOGIC;
			  SET : IN std_logic;
			  EN : IN STD_LOGIC;
			  Q : OUT STD_LOGIC
);
END COMPONENT;

--Sinais intermedios
SIGNAL Qout: STD_LOGIC_VECTOR (2 downto 0);		--Valor de contagem atual
SIGNAL Din: STD_LOGIC_VECTOR (2 downto 0);		--Proximo valor a ser armazenado

BEGIN

--Incremento
Din(0) <= CE xor Qout(0);											--Bit 0 inverte sempre que soma 1
Din(1) <= (Qout(0) and CE) xor Qout(1);						--Bit 1 inverte se bit 0 e CE = 1
Din(2) <= (Qout(1) and (Qout(0) and CE)) xor Qout(2);		--Bit 2 inverte se os bits 0..1 e CE = 1
	
FF0: FlipFlop PORT MAP(			--Instanciaçao do Flipflop1
			CLK => CLK,
			EN => CE,
			SET => '0',
			RESET => RESET,
			D => Din(0),
			Q => Qout(0));		
FF1: FlipFlop PORT MAP(			--Instanciaçao do Flipflop2
			CLK => CLK,
			EN => CE,
			SET => '0',
			RESET => RESET,
			D => Din(1),
			Q => Qout(1));
FF2: FlipFlop PORT MAP(			--Instanciaçao do Flipflop3
			CLK => CLK,
			EN => CE,
			SET => '0',
			RESET => RESET,
			D => Din(2),
			Q => Qout(2));
		
TC <= Qout(0) AND Qout(1) AND Qout(2);		--111
Q <= Qout;											
	
END arch_CR;
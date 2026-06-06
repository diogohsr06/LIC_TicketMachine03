---------------------------------------------------------------------------------------------
-- Key Control 
---------------------------------------------------------------------------------------------

library ieee;
use IEEE.std_logic_1164.all;

entity KeyControl is
    port (
			--Inputs
         CLK        : in  std_logic;				--Relogio da FPGA
         RESET      : in  std_logic;				--Clear da maquina
         Kack       : in  std_logic;				--Key Acknoledged (Tecla recebida)
         Kpress     : in  std_logic;				--Valor logico da pressao do botao
         TimerDone : in  std_logic;  				--Tempo excedido
         
			--Outputs
         Kval       : out std_logic;				--Key valid (Nova tecla pronta)
         Kscan      : out std_logic;				--Estado do varrimento
         TimerClr  : out std_logic   				--Reset ao temporizador
);
end KeyControl;

architecture arch_keycontrol of KeyControl is
--Registo 1 bit
component FlipFlop IS
    PORT( CLK   : in  std_logic;
          RESET : in  STD_LOGIC;
          SET   : in  std_logic;
          D     : IN  STD_LOGIC;
          EN    : IN  STD_LOGIC;
          Q     : out std_logic
    );
END component;

--Sinais intermedios
signal D0, D1, Q0, Q1: std_logic;					--Entradas e saidas dos FlipFlops
    
begin

--4 Estados: SCANNING, SENDING, SENT, REPEAT - 2 bits
F1: FlipFlop port map(CLK => CLK, RESET => RESET, SET => '0', D => D1, EN => '1', Q => Q1);		--Instanciaçao do Flipflop1
F2: FlipFlop port map(CLK => CLK, RESET => RESET, SET => '0', D => D0, EN => '1', Q => Q0);		--Instanciaçao do Flipflop2
    
--Logica do proximo estado
D1 <= (Kack and Q0 and not Q1) or (not Q0 and Q1) or (Q0 and Q1 and Kpress and not TimerDone);	
D0 <= ((Q0 xnor Q1) and Kpress) or (not Kack and (Q0 xor Q1));

--Sinais
Kval  <= not Q1 and Q0;			--Ativa no estado 01 (SENDING)
Kscan <= not Q1 and not Q0;	--Ativo no estado 00 (SCANNING)
TimerClr <= not (Q1 and Q0);	--Ativo no estado 11 (REPEAT)

end arch_keycontrol;
---------------------------------------------------------------------------------------------
-- Key Transmitter Control
---------------------------------------------------------------------------------------------

LIBRARY ieee;
USE ieee.STD_LOGIC_1164.ALL;

ENTITY KTC IS
    PORT (
			 --Inputs
          Load: IN STD_LOGIC;     		--Tecla pronta a enviar
          Tcount: IN STD_LOGIC;   		--Terminal Count (Fim da transmissao)
          clk: IN STD_LOGIC;				--Relogio da FPGA
          reset: IN STD_LOGIC;			--Clear da maquina
			 
			 --Outputs
          KBfree: OUT STD_LOGIC;  		--Pronto a receber tecla
          Ereg: OUT STD_LOGIC;    		--Enable register
          Ecounter: OUT STD_LOGIC;		--Enable counter (inicia o contador/transmissao)
          Rcounter: OUT STD_LOGIC);	--Reset counter (Reinicia o contador
END KTC;

ARCHITECTURE arch_KTC OF KTC IS

--Definiçao dos Estados
--Sinais intermedios
TYPE STATE_TYPE IS (STATE_WAITING_DATA, STATE_SEND_DATA);
SIGNAL CS, NS : STATE_TYPE; 
        
BEGIN

STATE_REG: PROCESS(clk, reset)
BEGIN
    IF reset = '1' THEN
        CS <= STATE_WAITING_DATA;
    ELSIF rising_edge(clk) THEN
        CS <= NS;
    END IF;
END PROCESS;
    
GENERATE_NEXT_STATE : PROCESS (CS, Load, Tcount) --Processo de registo de estado
BEGIN 

--Init
KBfree <= '0';
Ereg <= '0';
Ecounter <= '0';
Rcounter <= '0';
NS <= CS;
        
    CASE CS IS 
        WHEN STATE_WAITING_DATA =>				--Aguarda informaçao
            KBfree <= '1';   						--O sistema está livre para receber teclas
            Rcounter <= '1'; 					   --Mantém o contador de bits a 0
            IF (Load = '1') THEN					
                Ereg <= '1'; 						--Carrega a tecla para o registo 
                NS <= STATE_SEND_DATA;			--Pronto para enviar a tecla
            ELSE
                NS <= STATE_WAITING_DATA;
            END IF;
        WHEN STATE_SEND_DATA =>
            KBfree <= '0';   						--O key transmitter está ocupado
            Ecounter <= '1'; 						--Começa a contar
            IF (Tcount = '1') THEN				--Quando chega ao fim da contagem
                NS <= STATE_WAITING_DATA;		--Transmissão concluída, volta ao estado inicial
            ELSE 
                NS <= STATE_SEND_DATA;			--Senao, continua a transmitir
            END IF;  
    END CASE;
END PROCESS;
END arch_KTC;
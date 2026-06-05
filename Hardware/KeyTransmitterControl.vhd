LIBRARY ieee;
USE ieee.STD_LOGIC_1164.ALL;

ENTITY KTC IS
    PORT (
        Load : IN STD_LOGIC;     
        Tcount : IN STD_LOGIC;   
        clk : IN STD_LOGIC;
        reset : IN STD_LOGIC;
        KBfree : OUT STD_LOGIC;  
        Ereg : OUT STD_LOGIC;    
        Ecounter : OUT STD_LOGIC;
        Rcounter : OUT STD_LOGIC 
    );
END KTC;

ARCHITECTURE arch_KTC OF KTC IS

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
    
    GENERATE_NEXT_STATE : PROCESS (CS, Load, Tcount)
    BEGIN 
        
        KBfree <= '0';
        Ereg <= '0';
        Ecounter <= '0';
        Rcounter <= '0';
        NS <= CS;
        
        CASE CS IS 
        
            WHEN STATE_WAITING_DATA =>
                KBfree <= '1';   
                Rcounter <= '1'; 
                
                IF (Load = '1') THEN
                    Ereg <= '1'; 
                    NS <= STATE_SEND_DATA;
                ELSE
                    NS <= STATE_WAITING_DATA;
                END IF;
            
            WHEN STATE_SEND_DATA =>
                KBfree <= '0';   
                Ecounter <= '1'; 
                
                IF (Tcount = '1') THEN
                    NS <= STATE_WAITING_DATA;
                ELSE 
                    NS <= STATE_SEND_DATA;
                END IF;
                
        END CASE;
    END PROCESS;
END arch_KTC;
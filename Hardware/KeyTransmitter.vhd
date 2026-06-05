LIBRARY ieee;
USE ieee.STD_LOGIC_1164.ALL;

ENTITY KeyTransmitter IS
    PORT (
        clk: IN STD_LOGIC;
        Load: IN STD_LOGIC;                  
        D: IN STD_LOGIC_VECTOR(3 DOWNTO 0);  
        TX_clk: IN STD_LOGIC;                
        RESET : IN STD_LOGIC;
        KBfree: OUT STD_LOGIC;               
        TX_D: OUT STD_LOGIC                  
    );
END KeyTransmitter;

ARCHITECTURE arch_KT OF KeyTransmitter IS
    COMPONENT KTC
        PORT(
            Load : IN STD_LOGIC;
            Tcount : IN STD_LOGIC;
            clk : IN STD_LOGIC;
            reset : IN STD_LOGIC;
            KBfree : OUT STD_LOGIC;
            Ereg : OUT STD_LOGIC;
            Ecounter : OUT STD_LOGIC;
            Rcounter : OUT STD_LOGIC
        );
    END COMPONENT;
    COMPONENT Counter3 
        PORT(
            CE, CLK, RESET: IN STD_LOGIC;
            TC: OUT STD_LOGIC;
            Q : OUT STD_LOGIC_VECTOR(2 downto 0)
        );
    END COMPONENT;
        
    COMPONENT RegKT
        PORT(
            F : IN STD_LOGIC_VECTOR(3 DOWNTO 0);
            CE, RESET : IN STD_LOGIC;
            CLK : IN STD_LOGIC;
            Q : OUT STD_LOGIC_VECTOR(3 DOWNTO 0)
        );
    END COMPONENT;

    COMPONENT MUX6 
        PORT( 
            D : in STD_LOGIC_VECTOR(7 downto 0);
            S : in STD_LOGIC_VECTOR(2 downto 0);
            Q: out STD_LOGIC
        );
    END COMPONENT;

    SIGNAL TC_s, Ecounter_s, Rcounter_s, Ereg_s, KBfree_s: STD_LOGIC;
    SIGNAL Count_s : STD_LOGIC_VECTOR(2 DOWNTO 0);
    SIGNAL Q_s : STD_LOGIC_VECTOR(3 DOWNTO 0);
    SIGNAL D_mux : STD_LOGIC_VECTOR(7 DOWNTO 0);
    SIGNAL Mux_Out_s : STD_LOGIC;
    
BEGIN

    KBfree <= KBfree_s;

    D_mux(0) <= '0';        
    D_mux(1) <= '1';        
    D_mux(2) <= Q_s(0);     
    D_mux(3) <= Q_s(1);    
    D_mux(4) <= Q_s(2);     
    D_mux(5) <= Q_s(3);     
    D_mux(6) <= '0';        
    D_mux(7) <= '1';        

    TX_D <= Mux_Out_s WHEN KBfree_s = '0' ELSE '1';

    
    u_key_transmitter_control : KTC
        PORT MAP(
            Load => Load,
            Tcount => TC_s,
            clk => clk,
            reset => reset,
            KBfree => KBfree_s,
            Ereg => Ereg_s,
            Ecounter => Ecounter_s,
            Rcounter => Rcounter_s
        );

   
    u_reg : RegKT
        PORT MAP(
            F => D,
            CE => Ereg_s,
            RESET => reset,
            CLK => clk,
            Q => Q_s
        );

   
    u_KT_counter : Counter3
        PORT MAP(
            CE => Ecounter_s,
            CLK => TX_clk,
            RESET => Rcounter_s,
            TC => TC_s,
            Q => Count_s
        );

   
    u_KT_mux : MUX6
        PORT MAP(
            D => D_mux,
            S => Count_s,
            Q => Mux_Out_s
        );

END arch_KT;
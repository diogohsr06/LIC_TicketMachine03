---------------------------------------------------------------------------------------------
-- Key Decode
---------------------------------------------------------------------------------------------

library ieee;
use IEEE.std_logic_1164.all;

entity KeyDecode is
    port (
		  --Inputs
        Kack   : in  std_logic;								--Key Acknoledged (Tecla recebida)
        Tdelay : in  std_logic_vector(1 downto 0);		--Intervalo de tempo de repeat
        Rows   : in  std_logic_vector(3 downto 0);		--Linhas do teclado
        RESET  : in  std_logic;								--Clear da maquina
        Osc    : in  std_logic;								--Relogio da FPGA
        
		  --Outputs
        Cols   : out std_logic_vector(3 downto 0);		--Colunas do teclado
        K      : out std_logic_vector(3 downto 0);		--Codigo da tecla
        Kval   : out std_logic								--Key valid (Nova tecla pronta)
);
end KeyDecode;

architecture arch_keydecoder of KeyDecode is
--Varrimento do teclado
component KeyScan is
    port (
        Rows   : in  std_logic_vector(3 downto 0);
        Kscan  : in  std_logic;
        Osc    : in  std_logic;
        RESET  : in  std_logic;
            
        K      : out std_logic_vector(3 downto 0);
        Cols   : out std_logic_vector(3 downto 0);
        Kpress : out std_logic
    );
end component;
--Maquina de estados do Key Decode
component KeyControl is
    port (
        CLK        : in  std_logic;
        RESET      : in  std_logic;
        Kack       : in  std_logic;
        Kpress     : in  std_logic;
        TimerDone : in  std_logic;  
        Kval       : out std_logic;
        Kscan      : out std_logic;
        TimerClr  : out std_logic   
    );
end component;
--Temporizador de 500ms
component TDelay_Timer is
    port (
        Osc        : in  std_logic;
        TimerClr  : in  std_logic;
        Tdelay     : in  std_logic_vector(1 downto 0);
        TimerDone : out std_logic
    );
end component;

--Sinais intermedios
signal kscan_out      : std_logic;		--Kscan
signal kpress_out     : std_logic;		--KPress
signal timer_done_sig : std_logic;		--TimerDone
signal timer_clr_sig  : std_logic;		--TimerClear

begin
KS: KeyScan port map (						--Instanciaçao do varrimento do teclado
        Rows => Rows,
        Kscan => kscan_out,
        Osc => Osc,
        RESET => RESET,
        K => K,
        Cols => Cols,
        Kpress => kpress_out);
KC: KeyControl port map (					--Intanciaçao da maquina de estados
        CLK => Osc,
        RESET => RESET,
        Kack => Kack,
        Kpress => kpress_out,
        TimerDone => timer_done_sig, 
        Kval => Kval,
        Kscan => kscan_out,
        TimerClr => timer_clr_sig);
TIMER: TDelay_Timer port map (			--Instanciaçao do temporizador
		  Osc => Osc,
        TimerClr => timer_clr_sig,  
        Tdelay => Tdelay,         
        TimerDone => timer_done_sig);

end arch_keydecoder;
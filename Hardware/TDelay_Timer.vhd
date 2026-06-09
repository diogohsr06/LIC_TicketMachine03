---------------------------------------------------------------------------------------------
-- Temporizador 500ms
---------------------------------------------------------------------------------------------

library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

entity TDelay_Timer is
    port (
			 --Inputs
          Osc        : in  std_logic;   							--Relogio da FPGA                 
          TimerClr  : in  std_logic;                   		--Clear do temporizador
          Tdelay     : in  std_logic_vector(1 downto 0);		--Valor alvo de contagem
		  
		    --Outputs
          TimerDone : out std_logic                    		--Tempo excedido
);
end TDelay_Timer;

architecture arch_tmr of TDelay_Timer is
--Divisor de relogio
component CLKDIV500 is
    generic( div: natural := 25000000 );
    port ( 
        clk_in   : in  std_logic;
        clr      : in  std_logic;
        tick_out : out std_logic
);
end component;
--Contador 4 bits
component Counter is
    port(
         CE    : in  std_logic;
         CLK   : in  std_logic;
         RESET : in  std_logic;
         Q     : out std_logic_vector(3 downto 0)
);
end component;

--Sinais intermedios
signal tick_500ms : std_logic;								--Saida do CLKDIV (Ticks de 500ms)
signal count_out  : std_logic_vector(3 downto 0);		--Valor de contagem em ticks de 500ms

begin
UTICKGEN: CLKDIV500 generic map ( div => 12500000 ) port map (		--Instanciaçao do Divisor de relogio
          clk_in   => Osc,
          clr      => TimerClr,
          tick_out => tick_500ms);

    
UCOUNTER: Counter port map (													--Instanciaçao do contador
          CE    => tick_500ms, 
          CLK   => Osc,
          RESET => TimerClr,  
          Q     => count_out);

process(count_out, Tdelay)		--Processo para comparar o valor de contagem com o valor alvo
begin
    case Tdelay is
        when "00" =>   
            if count_out = "0001" then TimerDone <= '1'; else TimerDone <= '0'; end if;	--500ms
        when "01" =>   
            if count_out = "0010" then TimerDone <= '1'; else TimerDone <= '0'; end if;	--1000ms
        when "10" =>  
            if count_out = "0011" then TimerDone <= '1'; else TimerDone <= '0'; end if;	--1500ms
        when others => 
            if count_out = "0100" then TimerDone <= '1'; else TimerDone <= '0'; end if;	--2000ms
    end case;
end process;

end arch_tmr;
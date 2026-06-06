---------------------------------------------------------------------------------------------
-- Memory Address Control
---------------------------------------------------------------------------------------------

library ieee;
use ieee.std_logic_1164.all;

entity MAC is
    port(
		  --Inputs
        CLK: in  std_logic;							--Relogio da FPGA
        RESET: in  std_logic;							--Reinicia o sistema
        putget: in  std_logic;						--Seletor de endereço
        incPut: in  std_logic;						--Enable para incremento do endereço de escrita
        incGet: in  std_logic;						--Enable para incremento do endereço de leitura
		  
		  --Outputs
        S: out std_logic_vector(3 downto 0);		--Endereço da memoria
        full: out std_logic;							--Sinal de Ring Buffer cheio
        empty: out std_logic							--Sinal de Ring Buffer vazio
);
end MAC;

architecture arch_MAC of MAC is
--Contador de 5 bits
component Counter5 is 
    port(
        CLK: in std_logic;
        CE: in std_logic;
        RESET : in std_logic;
        Q: out std_logic_vector(4 downto 0)
);
end component;

--Sinais intermedios
signal Addr_put : std_logic_vector(4 downto 0);
signal Addr_get : std_logic_vector(4 downto 0);

begin

INCREMENTPUT: Counter5 port map(		--Instanciaçao do contador
			     CLK => CLK,
              CE  => incPut,
              RESET => RESET,
              Q   => Addr_put);
INCREMENTGET: Counter5 port map(		--Instanciaçao do contador
              CLK => CLK,
              CE  => incGet,
              RESET => RESET,
              Q   => Addr_get);

S <= Addr_get(3 downto 0) when putget = '0' else Addr_put(3 downto 0);													--Seleçao do endereço
full <= '1' when (Addr_put(3 downto 0) = Addr_get(3 downto 0)) and (Addr_put(4) /= Addr_get(4)) else '0';	--Logica do full
empty <= '1' when (Addr_put(3 downto 0) = Addr_get(3 downto 0)) and (Addr_put(4) = Addr_get(4)) else '0';	--Logica do empty

end arch_MAC;
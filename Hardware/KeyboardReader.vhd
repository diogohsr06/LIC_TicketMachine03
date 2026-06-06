---------------------------------------------------------------------------------------------
-- Keyboard Reader
---------------------------------------------------------------------------------------------

library ieee;
use IEEE.std_logic_1164.all;

entity KeyboardReader is
    port(
			--Inputs
			Rows: in std_logic_vector(3 downto 0);			--Linhas do teclado
			Tdelay: in std_logic_vector(1 downto 0);		--Intervalo de tempo de repeat
			RESET: in std_logic;									--Clear da maquina
			Osc: in std_logic;									--Relogio da FPGA
			TXclk: in std_logic;									--Relogio de transmissao
			
			--Outputs
			TXd: out std_logic;									--Bit de transmissao da data
			Cols: out std_logic_vector(3 downto 0));		--Colunas do teclado
end KeyboardReader;

architecture arch_kbreader of KeyboardReader is
--Descodificador de teclado
component KeyDecode is
    port
        (
         Kack: in std_logic;
			Tdelay: in std_logic_vector(1 downto 0);
			Rows: in std_logic_vector(3 downto 0);
			RESET: in std_logic;
			Osc: in std_logic;
			
			Cols: out std_logic_vector(3 downto 0);
			K: out std_logic_vector(3 downto 0);
			Kval: out std_logic);
end component;
--Armazenamento
component RingBuffer is
    port(
			CLK: in std_logic;
			RESET: in std_logic;
			CTS: in std_logic;
			DAV: in std_logic;
			Din: in std_logic_vector(3 downto 0);
			
			Wreg: out std_logic;
			Dout: out std_logic_vector(3 downto 0);
			DAC: out std_logic);
end component;
--Transmissao
component KeyTransmitter is
    port(
			CLK: in std_logic;
			Load: in std_logic;
			D: in std_logic_vector(3 downto 0);
			TXclk: in std_logic;
			RESET: in std_logic;
			
			KBfree: out std_logic;
			TXd: out std_logic);
end component;

--Sinais intermedios
signal DAC_out: std_logic;							--Data accepted
signal Kval_out: std_logic;						--Key valid
signal K_out: std_logic_vector(3 downto 0);	--Codigo da tecla (Key decode)
signal Q_out: std_logic_vector(3 downto 0);	--Codigo da tecla (Ring Buffer)
signal KBfree_out: std_logic;						--KBfree 
signal Wreg_out: std_logic;						--Wreg

begin
KD: KeyDecode port map (			--Instanciaçao do Descodificador de teclado
	 Kack => DAC_out,
	 Rows => Rows,
	 RESET => RESET,
	 Tdelay => Tdelay,
	 Osc => Osc,
	 Cols => Cols,
	 K => K_out,
	 Kval => Kval_out);
	 
RB: RingBuffer port map (			--Instanciaçao do bloco de armazenamento
	 CLK => Osc,
	 RESET => RESET,
	 CTS => KBfree_out,
	 DAV => Kval_out,
	 Din => K_out,
	 Wreg => Wreg_out,
	 Dout => Q_out,
	 DAC => DAC_out);
	 
KT: KeyTransmitter port map (		--Instanciaçao do bloco de transmissao
	 CLK => Osc,
	 Load => Wreg_out,
	 D => Q_out,
	 TXclk => TXclk,
	 RESET => RESET,
	 KBfree => KBfree_out,
	 TXd => TXd);
	 
end arch_kbreader;
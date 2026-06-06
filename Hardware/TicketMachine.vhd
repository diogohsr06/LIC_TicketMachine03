---------------------------------------------------------------------------------------------
-- TICKET MACHINE
---------------------------------------------------------------------------------------------
library ieee;
use IEEE.std_logic_1164.all;

entity TicketMachine is
    port
        (
			--Inputs
			M: in std_logic;																			--Chave de manutençao
			CoinId: in std_logic_vector(2 downto 0);											--Identificador da moeda
			Coin: in std_logic;																		--Chave de inserçao de moeda
			Rows: in std_logic_vector(3 downto 0);												--Linhas do teclado
			RESET: in std_logic;																		--Clear da maquina
			Tdelay: in std_logic_vector(1 downto 0);											--Intervalo de tempo de repeat
			Osc: in std_logic;																		--Relogio da FPGA
			CT: in std_logic;																			--Chave da colheita do bilhete
			
			--Outputs
			Cols: out std_logic_vector(3 downto 0);											--Colunas do teclado		
			D9: out std_logic;																		--MSB Data out (PELCD)
			D: out std_logic_vector(8 downto 0);												--9 LSB Data out (PELCD)
			HEX0, HEX1, HEX2, HEX3, HEX4, HEX5: out STD_LOGIC_VECTOR(7 downto 0);	--Displays de 7 segmentos
			Collect: out std_logic;																	--Sinal de recolha por parte da entidade consumidora
			Eject: out std_logic;																	--Sinal de devoluçao por parte da entidade consumidora
			Accept: out std_logic;																	--Sinal de aceitaçao por parte da entidade consumidora
			Prt: out std_logic);																		--Sinal de impressao do bilhete
end TicketMachine;

architecture arch_TM of TicketMachine is
--Leitor do teclado
component KeyboardReader is
    port(
			Rows: in std_logic_vector(3 downto 0);
			Tdelay: in std_logic_vector(1 downto 0);
			RESET: in std_logic;
			Osc: in std_logic;
			TXclk: in std_logic;
			TXd: out std_logic;
			Cols: out std_logic_vector(3 downto 0));
end component;
--Usbport
component UsbPort IS 
	PORT
	(
		inputPort:  IN  STD_LOGIC_VECTOR(7 DOWNTO 0);
		outputPort :  OUT  STD_LOGIC_VECTOR(7 DOWNTO 0)
	);
END component;
--Port Expander Liquid Cristal Display
component PELCD is
    port
        (
			SDX: in std_logic;
			SCLK: in std_logic;
			SS: in std_logic;
			RESET: in std_logic;
			D9: out std_logic;
			D: out std_logic_vector(8 downto 0));
end component;
--Ticket Dispenser
component TICKET_DISPENSER is
	port ( RT, Prt, CollectTicket: in STD_LOGIC;
			 O, D: in STD_LOGIC_VECTOR(3 downto 0);
			 Fn: out STD_LOGIC;
			 HEX0, HEX1, HEX2, HEX3, HEX4, HEX5: out STD_LOGIC_VECTOR(7 downto 0) );
end component;
--Port Expander Ticket Dispenser
component PETD is
    port
        (
			SDX: in std_logic;
			SCLK: in std_logic;
			SS: in std_logic;
			RESET: in std_logic;
			D9: out std_logic;
			D: out std_logic_vector(8 downto 0));
end component;

--Sinais intermedios
signal I0, I1, I2, I3, I4, I5, I6, I7: std_logic;	--Porto de entrada
signal O0, O1, O2, O3, O4, O5, O6, O7: std_logic;	--Porto de saida
signal Done: std_logic;										--Finish do Ticket Dispenser
signal Output_usb: std_logic_vector(7 downto 0);	--Porto de saida
signal D9_out: std_logic;									--MSB de data (PELCD)
signal D_out: std_logic_vector(8 downto 0);			--Bits restantes de data (PELCD)

--Instanciaçao
begin
--Sinais de entrada
I0 <= CoinId(0);
I1 <= CoinId(1);
I2 <= CoinId(2);
I3 <= Coin;
I6 <= M;

--Sinais do porto de saida
O0 <= Output_usb(0);
O1 <= Output_usb(1);
O2 <= Output_usb(2);
O3 <= Output_usb(3);
O4 <= Output_usb(4);
O5 <= Output_usb(5);
O6 <= Output_usb(6);
O7 <= Output_usb(7);

--Sinais de saida
Collect <= O6;
Eject <= O5;
Accept <= O4;
Prt <= D9_out;

KR: KeyboardReader port map(		--Instanciaçao do Leitor de teclado
	 Rows => Rows,
	 RESET => RESET,
	 Tdelay => Tdelay,
	 Osc => Osc,
	 TXclk => O7,
	 TXd => I7,
	 Cols => Cols);
	 
SRLCD: PELCD port map(				--Instanciaçao do PELCD
	 SDX => O0,
	 SCLK => O1,
	 SS => O2,
	 RESET => RESET,
	 D9 => D9,
	 D => D);
	 
SRTD: PETD port map(					--Instanciaçao do PETD
		SDX => O0,
		SCLK => O1,
		SS => O3,
		RESET => RESET,
		D9 => D9_out,
		D => D_out);
		
TD: TICKET_DISPENSER port map(	--Instanciaçao do Ticket Dispenser
	 RT => D_out(0),
	 Prt => D9_out,
	 CollectTicket => CT,
	 O(0) => D_out(1),
	 O(1) => D_out(2),
	 O(2) => D_out(3),
	 O(3) => D_out(4),
	 D(0) => D_out(5),
	 D(1) => D_out(6),
	 D(2) => D_out(7),
	 D(3) => D_out(8),
	 Fn => Done,
	 HEX0 => HEX0,
	 HEX1 => HEX1,
	 HEX2 => HEX2,
	 HEX3 => HEX3,
	 HEX4 => HEX4,
	 HEX5 => HEX5);
	 
UUSBPORT: UsbPort port map(		--Instanciaçao do UsbPort
			 inputPort(0) => I0,
			 inputPort(1) => I1,
			 inputPort(2) => I2,
			 inputPort(3) => I3,
			 inputPort(4) => Done,
			 inputPort(5) => '0',
			 inputPort(6) => I6,
			 inputPort(7) => I7,
			 outputPort => Output_usb);

end arch_TM;

---------------------------------------------------------------------------------------------
-- PINOS
---------------------------------------------------------------------------------------------
--M: SW[4]
--CoinId: SW[2..0]
--Coin: SW[3]
--Tdelay: SW[6..5]
--CT: SW[8]
--RESET: SW[9]

--Collect: LEDR[2]
--Eject: LEDR[1]
--Accept: LEDR[0]
--Prt: LEDR[9]

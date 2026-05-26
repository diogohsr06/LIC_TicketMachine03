library ieee;
use IEEE.std_logic_1164.all;

entity RBC is
    port(
			CLK: in std_logic;
			RESET: in std_logic;
			DAV: in std_logic;
			CTS: in std_logic;
			full: in std_logic;
			empty: in std_logic;
			
			Wr: out std_logic;
			selPG: out std_logic;
			incPut: out std_logic;
			incGet: out std_logic;
			Wreg: out std_logic;
			DAC: out std_logic);
end RBC;

architecture arch_RBC of RBC is
    type state_type is (IDLE, PUSH, WAIT_DAV, POP, LOAD_TX);
    signal state, next_state : state_type;
begin
    process(CLK, RESET)
    begin
        if RESET = '1' then
            state <= IDLE;
        elsif rising_edge(CLK) then
            state <= next_state;
        end if;
    end process;

    process(state, DAV, empty, full, CTS)
    begin
        -- Valores por defeito para evitar inferência de latches
        Wr <= '0'; selPG <= '0'; incPut <= '0'; incGet <= '0'; Wreg <= '0'; DAC <= '0';
        next_state <= state;

        case state is
            when IDLE =>
                if DAV = '1' and full = '0' then
                    next_state <= PUSH;
                elsif DAV = '0' and empty = '0' and CTS = '1' then
                    next_state <= POP;
                end if;

            when PUSH =>
                Wr <= '1';
                incPut <= '1';
                DAC <= '1'; -- Confirma a receção ao KeyDecode
                next_state <= WAIT_DAV;

            when WAIT_DAV =>
                DAC <= '1'; -- Mantém o acknowledge até o KeyDecode baixar o DAV
                if DAV = '0' then
                    next_state <= IDLE;
                end if;

            when POP =>
                selPG <= '1'; -- Prepara o endereço de leitura da RAM
                next_state <= LOAD_TX;

            when LOAD_TX =>
                selPG <= '1';
                incGet <= '1';
                Wreg <= '1'; -- Dispara o carregamento para o Transmissor
                next_state <= IDLE;
        end case;
    end process;
end arch_RBC;
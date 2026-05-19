library ieee;
use IEEE.std_logic_1164.all;

entity KTC is
    port(
        CLK      : in  std_logic;  -- Osc da FPGA
        RESET    : in  std_logic;
        Load     : in  std_logic;  -- Wreg do Ring Buffer (pulso 1 ciclo)
        cntDone  : in  std_logic;  -- contador chegou a "101"
        KBfree   : out std_logic;  -- '1' quando livre (para o Ring Buffer)
        Ereg     : out std_logic;  -- enable do registo de dados
        CE       : out std_logic;  -- enable do contador
        cntReset : out std_logic   -- reset do contador
    );
end KTC;

architecture arch_KTC of KTC is

    -- IDLE  : livre, à espera de dados do Ring Buffer
    -- STORE : guarda dado no registo (1 ciclo)
    -- SEND  : a transmitir ao Control (contador a correr)
    type STATE_TYPE is (IDLE, STORE, SEND);
    signal CS, NS : STATE_TYPE;

begin

    -- Registo de estado
    process(CLK, RESET)
    begin
        if RESET = '1' then
            CS <= IDLE;
        elsif rising_edge(CLK) then
            CS <= NS;
        end if;
    end process;

    -- Próximo estado
    process(CS, Load, cntDone)
    begin
        case CS is
            when IDLE =>
                if Load = '1' then
                    NS <= STORE;
                else
                    NS <= IDLE;
                end if;

            when STORE =>
                -- 1 ciclo para latchar os dados, depois começa a transmitir
                NS <= SEND;

            when SEND =>
                if cntDone = '1' then
                    NS <= IDLE;
                else
                    NS <= SEND;
                end if;
        end case;
    end process;

    -- Saídas (Moore)
    KBfree   <= '1' when CS = IDLE  else '0';
    Ereg     <= '1' when CS = STORE else '0';
    CE       <= '1' when CS = SEND  else '0';
    cntReset <= '1' when CS = IDLE  else '0';

end arch_KTC;
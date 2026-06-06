---------------------------------------------------------------------------------------------
-- Multiplexer 8:2
---------------------------------------------------------------------------------------------

LIBRARY ieee;
USE ieee.STD_LOGIC_1164.ALL;

ENTITY MUX8 IS
    PORT (
			 --Inputs
		    D: in STD_LOGIC_VECTOR(7 downto 0);		--Data
		    S: in STD_LOGIC_VECTOR(2 downto 0);		--Seletor
			 
			 --Outputs
		    Q: out STD_LOGIC									--Valor associado
    );
END MUX8;

ARCHITECTURE arch_MUX8 OF MUX8 IS
BEGIN
    process(D, S)									--Processo para selecionar o bit a transmitir de acordo com o valor de contagem
			begin
				case S is
					when "000" => Q <= D(0);
					when "001" => Q <= D(1);
					when "010" => Q <= D(2);
					when "011" => Q <= D(3);
					when "100" => Q <= D(4);
					when "101" => Q <= D(5);
					when "110" => Q <= D(6);
					when "111" => Q <= D(7);
					when others => Q <= '1'; 
				end case;
			end process;
END arch_MUX8;
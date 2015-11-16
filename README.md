# EC910Group
Group Project for Essex Module EC910 

The Agent-Based Random Model of Trading developed in class presents a simple artificial environment inspired by the Santa Fe Artificial Stock Market in Arthur et al. (1997) and LeBaron (2002). The model is made up of two classes of agents: Traders and Markets. Traders send ask and bid orders to the markets. Markets clear the market and update the prices. This project aims to extend he current model, using Java and JAS libraries, by introducing trading strategies following (but not limited to) Lo (2009) that will compete on an artificial market populated with current market data feeds.

The group should:
- select a set of strategies, one per student, motivating the choice and
specifying the real market data each strategy is supposed to compete on.
Strategies are not limited to only one market.
- Extends the DataImport.java class to accommodate the live data feed from
online resources (i.e. yahoo finance, google finance, etc)
- Initialize the artificial environment with real data at the beginning of the trading day. Prices will then update based on both trading activity of the
trading agents and real feeds.
- Extend Observer.java to visualize the trading outcomes of the agents
(P&L, trading positions, market indicators etc.)
- Save all outcomes of the simulation in a database by implementing a
DataExport.java class based on MySQL connector and basic SQL queries.

(Group Members: Pouyan, Mao, Jonathan, Liu)

/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashMap;

public class BaseballElimination {
    // 成员变量的表 ， 有一个maxflow的图 ， 还要有一个hashmap来根据队名快速找到对应的队伍信息
    private HashMap<String, Team> team_Info;
    private HashMap<Integer, String> idToName;

    private class Team {
        private String name;
        private int id;
        private int win;
        private int loss;
        private int to_Play;
        private int[] playWith;

        public Team(int id, String name, int win, int loss, int to_Play, int[] against_Games) {
            this.id = id;
            this.name = name;
            this.win = win;
            this.loss = loss;
            this.to_Play = to_Play;
            int size = against_Games.length;
            this.playWith = new int[size];
            for (int i = 0; i < size; ++i) {
                this.playWith[i] = against_Games[i];
            }
        }

        public int getId() {
            return id;
        }

        public int getLoss() {
            return loss;
        }

        public int getWin() {
            return win;
        }

        public int getTo_Play() {
            return to_Play;
        }

        public String getName() {
            return name;
        }

        public int get_Against(int id) {
            return playWith[id];
        }

        public int[] getPlayWith() {
            return playWith;
        }
    }

    public BaseballElimination(
            String filename) {// create a baseball division from given filename in format specified below
        if (filename == null) throw new IllegalArgumentException("null filename\n");
        In in = new In(filename);
        team_Info = new HashMap<String, Team>();
        idToName = new HashMap<Integer, String>();
        int teamNum = in.readInt();
        for (int i = 0; i < teamNum; ++i) {
            String name = in.readString();
            int win = in.readInt();
            int loss = in.readInt();
            int to_Play = in.readInt();
            int against_Games[] = new int[teamNum];
            for (int j = 0; j < teamNum; ++j) {
                against_Games[j] = in.readInt();
            }
            Team team = new Team(i, name, win, loss, to_Play, against_Games);
            team_Info.put(team.getName(), team);
            idToName.put(i, name);
        }
    }

    public int numberOfTeams() {
        return team_Info.size();
    }// number of teams

    public Iterable<String> teams() {
        return team_Info.keySet();
    }                                // all teams

    public int wins(String team) {// number of wins for given team
        validate(team);
        return team_Info.get(team).getWin();
    }

    public int losses(String team) {// number of losses for given team
        validate(team);
        return team_Info.get(team).getLoss();
    }

    public int remaining(String team) {// number of remaining games for given team
        validate(team);
        return team_Info.get(team).getTo_Play();
    }

    public int against(String team1,
                       String team2) {// number of remaining games between team1 and team2
        validate(team1);
        validate(team2);
        // TODO:finish this
        Team team1_ = team_Info.get(team1);
        Team team2_ = team_Info.get(team2);
        return team1_.get_Against(team2_.id);
    }

    public boolean isEliminated(String team) { // is given team eliminated? 可以被成功编排
        validate(team);
        if (TrivialElimination(team)) return true;
        int GameLeft = getGameLeft(team);
        int teamNum = team_Info.size();
        int numOfGameVertices = teamNum * (teamNum - 1) / 2;
        FlowNetwork flowNetwork = new FlowNetwork(
                1 + numOfGameVertices + team_Info.size() - 1 + 1);
        Intialize_FlowNetwork(flowNetwork, team);
        FordFulkerson fordFulkerson = new FordFulkerson(flowNetwork, 0,
                                                        1 + numOfGameVertices + teamNum - 1 + 1
                                                                - 1);
        return fordFulkerson.value() < GameLeft; // 说明找不到一种限制的方法，直接可以eliminate了
    }

    /**
     * 利用还剩下的总的比赛场数 = 除去team之外的所有场的总和 - team和这些其他队伍打的
     *
     * @param team
     * @return
     */
    private int getGameLeft(String team) {
        validate(team);
        int GameLeft = 0;
        Team check_Team = team_Info.get(team);
        for (int i = 0; i < team_Info.size(); ++i) {
            if (i == check_Team.getId()) continue;
            for (int j = i + 1; j < team_Info.size(); ++j) {
                if (j == check_Team.getId()) continue;
                GameLeft += team_Info.get(idToName.get(i)).get_Against(j);
            }
        }
        return GameLeft;
    }

    // 本质上就是找没有满的边， 如果边没有满那么对应的team就会和t一侧
    // 因为还需要用到forkFulkerson这个对象，所以需要修改Nontrivial这个函数的参数（有没有更好的设计思路呢？）
    public Iterable<String> certificateOfElimination(
            // subset R of teams that eliminates given team; null if not eliminated
            String team) {
        validate(team);
        // trivial
        ArrayList<String> Eliminators = new ArrayList<String>();
        Team checkTeam = team_Info.get(team);
        ;
        // 这种情况就是 w[i] > w[x] + t[x] 导致被消灭，找到这些就行了
        if (TrivialElimination(team)) {
            for (Team team_ : team_Info.values()) {
                if (team_.getWin() > checkTeam.getWin() + checkTeam.getTo_Play())
                    Eliminators.add(team_.getName());
            }
            return Eliminators;
        }
        // nontrivail
        int GameLeft = getGameLeft(team);
        int teamNum = team_Info.size();
        int numOfGameVertices = teamNum * (teamNum - 1) / 2;
        FlowNetwork flowNetwork = new FlowNetwork(1 + numOfGameVertices + numberOfTeams() + 1);
        Intialize_FlowNetwork(flowNetwork, team);
        FordFulkerson fordFulkerson = new FordFulkerson(flowNetwork, 0,
                                                        1 + numOfGameVertices + teamNum - 1 + 1
                                                                - 1);
        // 这里判断是否可以有对应的调配策略让team获得第一名
        if (fordFulkerson.value() >= GameLeft) return null;// 可以控制在范围内,not eliminate,仍有机会

        for (int i = 0; i < team_Info.size(); ++i) {
            if (i == checkTeam.getId()) continue;
            if (fordFulkerson.inCut(teamId2Vertex(i, numOfGameVertices, checkTeam.getId()))) {
                Eliminators.add(idToName.get(i));
            }
        }
        return Eliminators;
    }

    /**
     * 检测对应id的Team是否有可能不通过maxflow图来判别可以消除
     * 主要有两种方法 ， 一种是看全部都赢 ，另一种是通过剩下还能产生出的胜场求出最终的平均胜场，看看对应的全赢能否
     *
     * @param id
     * @return
     */
    private boolean TrivialElimination(String team) {
        Team check_Team = team_Info.get(team);
        for (Team team_ : team_Info.values()) {
            if (check_Team.getId() == team_.getId()) continue;
            if (check_Team.getWin() + check_Team.getTo_Play() < team_.win) return true;
        }
        return false;
    }


    private int teamId2Vertex(int id, int numOfGameVertices, int checkTeam_Id) {
        if (checkTeam_Id == id) throw new IllegalArgumentException();
        else if (checkTeam_Id > id) return id + numOfGameVertices + 1;
        else return id + numOfGameVertices;
    }

    private void Intialize_FlowNetwork(FlowNetwork flowNetwork, String team) {
        Team checkTeam = team_Info.get(team);
        int GameVerticesIndex = 1;
        int teamNum = team_Info.size(); // 减去要check的那一个Team
        int numOfGameVertices = teamNum * (teamNum - 1) / 2;
        // 更新s和game，game和team之间的连线
        for (int i = 0; i < teamNum; ++i) { // begin++是指下一次要从begin_Id++的位置开始遍历了
            if (i == checkTeam.getId()) continue;
            Team team_ = team_Info.get(idToName.get(i)); // 确保是按照顺序访问的
            for (int j = 0; j < teamNum; ++j) { // 这里的j是对手， i是自己的id , 只有大于自己的序号的对手才会添加新的边
                if (j == checkTeam.getId() || j <= i) continue;
                flowNetwork.addEdge(new FlowEdge(0, GameVerticesIndex, team_.get_Against(j)));
                flowNetwork.addEdge(new FlowEdge(GameVerticesIndex,
                                                 teamId2Vertex(j, numOfGameVertices,
                                                               checkTeam.getId()),
                                                 Double.POSITIVE_INFINITY));
                flowNetwork.addEdge(
                        new FlowEdge(GameVerticesIndex, teamId2Vertex(i, numOfGameVertices,
                                                                      checkTeam.getId()),
                                     Double.POSITIVE_INFINITY));
                GameVerticesIndex++;
            }
        }
        // 更新vertice和终点的连线
        int t = 1 + numOfGameVertices + teamNum - 1 + 1 - 1;
        for (int i = 0; i < teamNum; ++i) {
            if (i == checkTeam.getId()) continue;
            double delta = checkTeam.getWin() + checkTeam.getTo_Play()
                    - team_Info.get(idToName.get(i)).getWin();
            if (delta < 0) throw new RuntimeException("delta is negative\n");
            flowNetwork.addEdge(
                    new FlowEdge(teamId2Vertex(i, numOfGameVertices, checkTeam.getId()), t,
                                 delta));
        }
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }

    private void validate(String team) {
        if (team == null || team_Info.get(team) == null) throw new IllegalArgumentException();
    }
}

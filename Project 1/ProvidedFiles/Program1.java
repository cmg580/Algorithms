/*
 * Name: Christian Gil
 * EID: cmg4463
 */

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Your solution goes in this class.
 * 
 * Please do not modify the other files we have provided for you, as we will use
 * our own versions of those files when grading your project. You are
 * responsible for ensuring that your solution works with the original version
 * of all the other files we have provided for you.
 * 
 * That said, please feel free to add additional files and classes to your
 * solution, as you see fit. We will use ALL of your additional files when
 * grading your solution.
 */
public class Program1 extends AbstractProgram1 {
    /**
     * Returns the ranking of each server's preferance for a user. Index is server number
     * and value of index is ranking of user at that server
     */
    public int [] getUserRankingForServers(Matching allocation, int user){
        ArrayList<ArrayList<Integer>> s_list = allocation.getServerPreference();
        int [] preflist = new int[s_list.size()];

        for (int i = 0; i < s_list.size(); i++)
        {
            ArrayList<Integer> userList = s_list.get(i);
            preflist[i] = userList.indexOf(user);
        }

        return preflist;
    }

    /**
     * Returns True or False if all the slots for jobs on the server are filled
     */
    public boolean allJobsFilled(Matching allocation)
    {
        ArrayList<Integer> current_matches = allocation.getUserMatching();
        int [] servers_filled = new int[ allocation.getServerCount() ];

        // Get the number of users on each server
        for (int user = 0; user < current_matches.size(); user++)
        {
            int server = current_matches.get(user);
            if (server != -1)
            {
                servers_filled[server] += 1;
            }
        }

        // Check that the number of slots is filled
        ArrayList<Integer> server_slots = allocation.getServerSlots();
        int [] server_slot_list = new int[ server_slots.size() ];

        // Subtract the number of users on each server from the number of slots
        for (int i = 0; i < server_slot_list.length; i++)
        {
            server_slot_list[i] = server_slots.get(i) - servers_filled[i];
            // If too many users are on one server, then this is unstable
            if (server_slot_list[i] < 0){
                return false;
            }
        }

        // Determine if all the slots are filled
        int sum = 0;
        for (int i = 0; i < server_slot_list.length; i++){
            sum += server_slot_list[i];
        }

        if (sum != 0)
        {
            return false;
        }
        else
        {
            return true;
        }

    }


    /**
     * Determines whether a candidate Matching represents a solution to the
     * Stable Marriage problem. Study the description of a Matching in the
     * project documentation to help you with this.
     */
    public boolean isStableMatching(Matching allocation) {
        ArrayList<ArrayList<Integer>> s_list = allocation.getServerPreference();
        ArrayList<ArrayList<Integer>> u_list = allocation.getUserPreference();
        ArrayList<Integer> current_matches = allocation.getUserMatching();

        for (int user = 0; user < current_matches.size(); user++)
        {
            int server = current_matches.get(user);
            // If the server is -1, that means that the current user is not paired with any server
            if (server == -1)
            {
                continue;
            }
            // Since the user is paired with a server, check if it is stable
            else
            {
                // Get a list of how all the users rank for the current server
                ArrayList<Integer> sel_server_pref_list = s_list.get(server);

                // 
                // Instability 1: u & s (user & server); u' is alone (server_for_fav == -1); 
                // s preferes u' to u (by essense of favorite == user being false)
                //

                // For each person in the server's favorite list
                for (int u = 0; u < sel_server_pref_list.size(); u++)
                {
                    //  If the current user is the favorite of the server
                    int favorite = sel_server_pref_list.get(u);

                    if (favorite == user)
                    {
                        break;
                    }
                    // If the server prefers someone over the current user, and that user is available, this is unstable
                    int server_for_fav = current_matches.get( favorite );
                    if (server_for_fav == -1 )
                    {
                        return false;
                    }
                    // If the person is unavailable, check if they prefer this server over their current
                    else
                    {
                        //
                        // Instability 2: u & s (user & server); u' & s' (favorite & server_for_fav); 
                        // s perfers u' to u (by essence of favorite == u being false);
                        // u' prefers s to s' (determination below);
                        //

                        // Determine if the server_for_fav prefers (user) over (favorite)
                        int [] server_favorite_prefList = getUserRankingForServers(allocation, favorite);
                        int rank_of_server = server_favorite_prefList[server];
                        int rank_of_curr_server = server_favorite_prefList[server_for_fav];
                        if (rank_of_server < rank_of_curr_server){
                            return false;
                        }

                        // Determine if the favorite prefers (server) over (server_for_fav)
                        ArrayList<Integer> favorite_server_list = u_list.get(favorite);
                        rank_of_server = favorite_server_list.indexOf(server);
                        rank_of_curr_server = favorite_server_list.indexOf(server_for_fav);
                        if (rank_of_server < rank_of_curr_server)
                        {
                            return false;
                        }
                    }
                }

            }
        }

        if ( allJobsFilled(allocation) )
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Determines a solution to the Stable Marriage problem from the given input
     * set. Study the project description to understand the variables which
     * represent the input to your solution.
     * 
     * @return A stable Matching.
     */
    public Matching stableMarriageGaleShapley(Matching allocation) {
        // Initially all servers S are free and no user U is assigned a server.
        ArrayList<ArrayList<Integer>> server_pref = allocation.getServerPreference();
        ArrayList<ArrayList<Integer>> user_pref = allocation.getUserPreference();
        ArrayList<Integer> server_slots = new ArrayList<Integer>( allocation.getServerSlots().size() );
        ArrayList<Integer> potential_matches = new ArrayList<Integer>( allocation.getUserCount() );

        // Create a deepclone of the allocation.getServerSlots() object
        for (Integer i : allocation.getServerSlots() ){
            server_slots.add( i );
        }



        // Set all the potential matches as unmatches
        for (int i = 0; i < allocation.getUserCount(); i++)
        {
            potential_matches.add(-1);
        }

        int current_server = 0;
        // While there are servers S with slots s that are free.
        while ( allocation.totalServerSlots() > 0 ) 
        {
            // Choose a server S with free slots
            if ( server_slots.get( current_server ) == 0 )
            {
                current_server += 1;
                // If we ran out of servers, we are done
                if ( current_server >= allocation.getServerCount() ) 
                { 
                    current_server = 0; 
                }
            }

            // Let F be the highest-ranked user in S’s preference list to whom F is unassigned
            ArrayList<Integer> fav_list = server_pref.get( current_server );
            int f = fav_list.get( 0 );
            // If F is free then
            if ( potential_matches.get(f) == -1 )
            {
                // (F, S) link together
                potential_matches.set(f, current_server);
                server_slots.set ( current_server, server_slots.get( current_server ) - 1);
                server_pref.get( current_server ).remove(0);
            }
            // // Else F is already assigned to server S’
            else
            {
                // If F prefers S’ to S then
                ArrayList<Integer> user_fav_list = user_pref.get(f);
                int pos_of_S = user_fav_list.indexOf( current_server );
                int pos_of_Sprime = user_fav_list.indexOf( potential_matches.get(f) );
                if (pos_of_Sprime < pos_of_S)
                {
                    // F remains free
                    continue;
                }
                // Else F prefers S to S’
                else
                {
                    // (F,S) link together
                    potential_matches.set(f, current_server);
                    server_slots.set ( current_server, server_slots.get( current_server ) - 1);
                    server_pref.get( current_server ).remove(0);
                    // S’ becomes free
                    server_slots.set ( potential_matches.get(f), server_slots.get( potential_matches.get(f) ) + 1);
                }
            }
        }

        // Since we have all the stable matches, set the allocation to know that
        allocation.setUserMatching( potential_matches );
        return allocation;
    }
}

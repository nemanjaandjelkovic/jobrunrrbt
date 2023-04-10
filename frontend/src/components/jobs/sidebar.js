import React, {useState} from 'react';
import {Link} from "react-router-dom";
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import Chip from '@material-ui/core/Chip';
import {AllInbox, Schedule} from "@material-ui/icons";
import {AlertCircleOutline, Check, Cogs, Delete, TimerSand} from "mdi-material-ui";
import statsState from "../../StatsStateContext";
import FormDialog from "../custom/sendArea";
import Button from "@material-ui/core/Button";

const categories = [
    {name: "scheduled", state: "SCHEDULED", label: "Scheduled", icon: <Schedule/>, component: Link},
    {name: "enqueued", state: "ENQUEUED", label: "Enqueued", icon: <TimerSand/>, component: Link},
    {name: "processing", state: "PROCESSING", label: "Processing", icon: <Cogs/>, component: Link},
    {name: "succeeded", state: "SUCCEEDED", label: "Succeeded", icon: <Check/>, component: Link},
    {name: "failed", state: "FAILED", label: "Failed", icon: <AlertCircleOutline/>, component: Link},
    {name: "deleted", state: "DELETED", label: "Deleted", icon: <Delete/>, component: Link},
    {name: "send", state: "SEND", label: "Jobs Re-Enqueue", icon: <AllInbox/>, component: Button}
];
const Sidebar = () => {
    const [stats, setStats] = React.useState(statsState.getStats());
    React.useEffect(() => {
        statsState.addListener(setStats);
        return () => statsState.removeListener(setStats);
    }, [])

    const [isOpen, setIsOpen] = useState(false);

    const handleOpen = () => {
        setIsOpen(!isOpen);
    };


    return (
        <List>
            <List component="div" disablePadding>
                {categories.map(({name, state, label, icon, component}) => (
                    component === Link ? (
                            <ListItem id={`${name}-menu-btn`} button key={label} title={label} component={component}
                                      to={`/dashboard/jobs?state=${state}`}>
                                <ListItemIcon>{icon}</ListItemIcon>
                                <ListItemText primary={label}/>
                                <Chip label={stats[name]}/>
                            </ListItem>)
                        : (<ListItem id={`${name}-menu-btn`} button key={label} title={label} component={component}
                                     onClick={() => handleOpen()}>
                            <ListItemIcon>{icon}</ListItemIcon>
                            <ListItemText primary={label} style={{textTransform: "capitalize"}}/>
                            <FormDialog isDialogOpened={isOpen}
                                        handleCloseDialog={() => setIsOpen(false)}>

                            </FormDialog>
                        </ListItem>)
                ))}
            </List>
        </List>
    );
};

export default Sidebar;
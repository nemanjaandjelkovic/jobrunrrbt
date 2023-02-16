import * as React from 'react';
import Button from '@material-ui/core/Button';
import TextField from '@material-ui/core/TextField';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle';
import {useMediaQuery, useTheme} from "@material-ui/core";
import axios from "axios";

export default function FormDialog({isDialogOpened, handleCloseDialog}) {

    const [data, setData] = React.useState("");

    const handleClose = () => {
        handleCloseDialog(false);
    };

    const handleSend = (e) => {
        data.split("/").forEach((id) => {
            axios.post(`/api/jobs/${id}/requeue`)
        })
        window.location.reload(true);
        handleCloseDialog(false);
    };

    const theme = useTheme();
    const fullScreen = useMediaQuery(theme.breakpoints.down('lg'));


    return (<div>
            <Dialog open={isDialogOpened} onClose={handleClose} fullScreen={true} style={{padding: "5%"}}>
                <DialogTitle>Input Id list for requeue</DialogTitle>
                <DialogContent>
                    <TextField
                        autoFocus
                        margin="dense"
                        id="name"
                        label="Please enter list of id jobs"
                        placeholder={"id/id/id"}
                        type="text"
                        fullWidth
                        variant="standard"
                        multiline
                        minRows={10}
                        onChange={(e) => setData(e.target.value)}
                    />
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose}>Cancel</Button>
                    <Button onClick={handleSend}>Send</Button>
                </DialogActions>
            </Dialog>
        </div>);
}